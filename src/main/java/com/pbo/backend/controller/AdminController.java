package com.pbo.backend.controller;

import com.pbo.backend.model.*;
import com.pbo.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private PasswordEncoder passwordEncoder;

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
        model.addAttribute("members", memberService.getAllMembers());
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
                             @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.MEMBER);
        member.setUser(user);
        memberService.saveMember(member);
        return "redirect:/admin/members";
    }

    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }

    // === TRAINER ===
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
                              @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.PT);
        trainer.setUser(user);
        trainerService.saveTrainer(trainer);
        return "redirect:/admin/trainers";
    }

    @GetMapping("/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
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
                                 @RequestParam(required = false) String catatan) {
        ptRequestService.approveRequest(id, catatan);
        return "redirect:/admin/requests";
    }

    @PostMapping("/requests/reject/{id}")
    public String rejectRequest(@PathVariable Long id,
                                @RequestParam(required = false) String catatan) {
        ptRequestService.rejectRequest(id, catatan);
        return "redirect:/admin/requests";
    }
}