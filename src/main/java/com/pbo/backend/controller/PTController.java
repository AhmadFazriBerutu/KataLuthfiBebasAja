package com.pbo.backend.controller;

import com.pbo.backend.model.Trainer;
import com.pbo.backend.service.PTRequestService;
import com.pbo.backend.service.ScheduleService;
import com.pbo.backend.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pt")
public class PTController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PTRequestService ptRequestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Trainer trainer = trainerService.getTrainerByUsername(auth.getName());
        model.addAttribute("trainer", trainer);
        model.addAttribute("schedules", scheduleService.getSchedulesByTrainer(trainer));
        model.addAttribute("members", ptRequestService.getApprovedMembersByTrainer(trainer));
        return "pt/dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Authentication auth) {
        Trainer trainer = trainerService.getTrainerByUsername(auth.getName());
        model.addAttribute("schedules", scheduleService.getSchedulesByTrainer(trainer));
        return "pt/schedule";
    }

    @GetMapping("/members")
    public String members(Model model, Authentication auth) {
        Trainer trainer = trainerService.getTrainerByUsername(auth.getName());
        model.addAttribute("requests", ptRequestService.getRequestsByTrainer(trainer));
        return "pt/members";
    }
}