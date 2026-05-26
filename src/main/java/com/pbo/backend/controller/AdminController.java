package com.pbo.backend.controller;

import com.pbo.backend.model.*;
import com.pbo.backend.repository.UserRepository;
import com.pbo.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private PTRequestService ptRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PembayaranService pembayaranService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMembers", memberService.getAllMembers().size());
        model.addAttribute("totalTrainers", trainerService.getAllTrainers().size());
        model.addAttribute("pendingRequests", ptRequestService.getPendingRequests().size());
        return "admin/dashboard";
    }

    // === MEMBER ===
    @GetMapping("/members")
    public String members(Model model) {
        var members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("totalPremium", members.stream().filter(m -> m.getPaket() == Member.Paket.PREMIUM).count());
        model.addAttribute("totalBasic", members.stream().filter(m -> m.getPaket() == Member.Paket.BASIC).count());
        return "admin/member/list";
    }

    @GetMapping("/members/add")
    public String addMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "admin/member/add";
    }

    @PostMapping("/members/save")
    public String saveMember(@ModelAttribute Member member,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam(required = false) String metodePembayaran,
                             @RequestParam(required = false) Double nominal,
                             Model model) {
        // 1. Validasi Duplikat Username
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("member", member);
            model.addAttribute("errorUsername", "Username '" + username + "' sudah digunakan. Pilih username lain.");
            return "admin/member/add";
        }

        // 2. Buat dan SAVE User terlebih dahulu untuk menghindari TransientPropertyValueException
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.MEMBER);
        User savedUser = userRepository.save(user); // Mencegah error transient object

        // 3. Pasang User yang sudah memiliki ID ke objek Member, lalu simpan
        member.setUser(savedUser);
        Member savedMember = memberService.saveMember(member);

        // 4. Simpan data pembayaran jika paket PREMIUM
        if (savedMember.getPaket() == Member.Paket.PREMIUM) {
            pembayaranService.buatPembayaran(savedMember, metodePembayaran, nominal);
        }

        return "redirect:/admin/members";
    }

    @GetMapping("/members/edit/{id}")
    public String editMemberForm(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id).orElseThrow();
        model.addAttribute("member", member);
        return "admin/member/edit";
    }

    @PostMapping("/members/update/{id}")
    public String updateMember(@PathVariable Long id,
                               @ModelAttribute Member form,
                               @RequestParam String username,
                               Model model) {
        Member member = memberService.getMemberById(id).orElseThrow();
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent() && !existing.get().getId().equals(member.getUser().getId())) {
            model.addAttribute("member", member);
            model.addAttribute("errorUsername", "Username '" + username + "' sudah digunakan. Pilih username lain.");
            return "admin/member/edit";
        }
        member.setNama(form.getNama());
        member.setEmail(form.getEmail());
        member.setTelepon(form.getTelepon());
        member.setPaket(form.getPaket());
        member.getUser().setUsername(username);

        // Pastikan cascade/update user berjalan aman
        userRepository.save(member.getUser());
        memberService.saveMember(member);
        return "redirect:/admin/members";
    }

    // === PT REQUESTS ===
    @GetMapping("/trainers")
    public String trainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "admin/trainer/list";
    }

    @GetMapping("/trainers/add")
    public String addTrainerForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "admin/trainer/add";
    }

    @PostMapping("/trainers/save")
    public String saveTrainer(@ModelAttribute Trainer trainer,
                              @RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("trainer", trainer);
            model.addAttribute("errorUsername", "Username '" + username + "' sudah digunakan. Pilih username lain.");
            return "admin/trainer/add";
        }

        // Buat dan SAVE User terlebih dahulu untuk menghindari TransientPropertyValueException
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.PT);
        User savedUser = userRepository.save(user); // Mencegah error transient object

        trainer.setUser(savedUser);
        trainerService.saveTrainer(trainer);
        return "redirect:/admin/trainers";
    }

    @GetMapping("/trainers/edit/{id}")
    public String editTrainerForm(@PathVariable Long id, Model model) {
        Trainer trainer = trainerService.getTrainerById(id).orElseThrow();
        model.addAttribute("trainer", trainer);
        return "admin/trainer/edit";
    }

    @PostMapping("/trainers/update/{id}")
    public String updateTrainer(@PathVariable Long id,
                                @ModelAttribute Trainer form,
                                @RequestParam String username,
                                Model model) {
        Trainer trainer = trainerService.getTrainerById(id).orElseThrow();
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent() && !existing.get().getId().equals(trainer.getUser().getId())) {
            model.addAttribute("trainer", trainer);
            model.addAttribute("errorUsername", "Username '" + username + "' sudah digunakan. Pilih username lain.");
            return "admin/trainer/edit";
        }
        trainer.setNama(form.getNama());
        trainer.setEmail(form.getEmail());
        trainer.setTelepon(form.getTelepon());
        trainer.setSpesialisasi(form.getSpesialisasi());
        trainer.getUser().setUsername(username);

        userRepository.save(trainer.getUser());
        trainerService.saveTrainer(trainer);
        return "redirect:/admin/trainers";
    }

    // === PT REQUESTS ===
    @GetMapping("/requests")
    public String requests(Model model) {
        model.addAttribute("requests", ptRequestService.getAllRequests());
        return "admin/requests";
    }

    @PostMapping("/requests/approve/{id}")
    public String approveRequest(@PathVariable Long id,
                                 @RequestParam(required = false) String catatan,
                                 org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        try {
            ptRequestService.approveRequest(id, catatan);
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/requests";
    }

    @PostMapping("/requests/reject/{id}")
    public String rejectRequest(@PathVariable Long id,
                                @RequestParam(required = false) String catatan) {
        ptRequestService.rejectRequest(id, catatan);
        return "redirect:/admin/requests";
    }

    @PostMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }

    @PostMapping("/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return "redirect:/admin/trainers";
    }

    // === PEMBAYARAN ===
    @GetMapping("/pembayaran")
    public String pembayaran(Model model) {
        var list = pembayaranService.getSemuaPembayaran();
        double totalPemasukan = list.stream()
                .filter(p -> p.getStatus() == com.pbo.backend.model.Pembayaran.StatusPembayaran.LUNAS)
                .mapToDouble(p -> p.getNominal() != null ? p.getNominal() : 0)
                .sum();
        long totalLunas = list.stream()
                .filter(p -> p.getStatus() == com.pbo.backend.model.Pembayaran.StatusPembayaran.LUNAS)
                .count();
        model.addAttribute("pembayaranList", list);
        model.addAttribute("totalPemasukan", (long) totalPemasukan);
        model.addAttribute("totalLunas", totalLunas);
        return "admin/pembayaran";
    }
}