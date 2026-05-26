package com.pbo.backend.controller;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.PTRequest;
import com.pbo.backend.model.Schedule;
import com.pbo.backend.model.Trainer;
import com.pbo.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PTRequestService ptRequestService;

    @GetMapping("/list")
    public String list(Model model) {
        var members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("totalActive", members.stream().filter(m -> m.isActive()).count());
        model.addAttribute("totalInactive", members.stream().filter(m -> !m.isActive()).count());
        return "member/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Member member = memberService.getMemberByUsername(auth.getName());
        model.addAttribute("member", member);
        return "member/dashboard";
    }

    @GetMapping("/trainers")
    public String listTrainers(Model model, Authentication auth) {
        Member member = memberService.getMemberByUsername(auth.getName());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        model.addAttribute("isPremium", member.getPaket() == Member.Paket.PREMIUM);
        return "member/trainers";
    }

    @GetMapping("/trainers/{id}/schedules")
    public String trainerSchedules(@PathVariable Long id, Model model) {
        Trainer trainer = trainerService.getTrainerById(id).orElseThrow();
        List<Schedule> schedules = scheduleService.getAvailableSchedules(trainer);
        model.addAttribute("trainer", trainer);
        model.addAttribute("schedules", schedules);
        return "member/trainer-schedules";
    }

    @PostMapping("/request-pt")
    public String requestPT(@RequestParam Long trainerId,
                            @RequestParam Long scheduleId,
                            Authentication auth) {
        Member member = memberService.getMemberByUsername(auth.getName());

        if (member.getPaket() != Member.Paket.PREMIUM) {
            return "redirect:/member/trainers?error=notpremium";
        }

        Trainer trainer = trainerService.getTrainerById(trainerId).orElseThrow();
        Schedule schedule = scheduleService.getScheduleById(scheduleId);

        PTRequest request = new PTRequest();
        request.setMember(member);
        request.setTrainer(trainer);
        request.setSchedule(schedule);
        request.setStatus(PTRequest.Status.PENDING);
        ptRequestService.saveRequest(request);

        return "redirect:/member/my-requests";
    }

    @GetMapping("/my-requests")
    public String myRequests(Model model, Authentication auth) {
        Member member = memberService.getMemberByUsername(auth.getName());
        model.addAttribute("requests", ptRequestService.getRequestsByMember(member));
        return "member/my-requests";
    }

    // === FITUR BARU: EDIT PROFIL MEMBER ===
    @GetMapping("/profile")
    public String editProfileForm(Model model, Authentication auth) {
        Member member = memberService.getMemberByUsername(auth.getName());
        model.addAttribute("member", member);
        return "member/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Member form, Authentication auth) {
        Member member = memberService.getMemberByUsername(auth.getName());

        // Update data profil dasar dari input form
        member.setNama(form.getNama());
        member.setEmail(form.getEmail());
        member.setTelepon(form.getTelepon());

        memberService.saveMember(member);
        return "redirect:/member/profile?success";
    }
}