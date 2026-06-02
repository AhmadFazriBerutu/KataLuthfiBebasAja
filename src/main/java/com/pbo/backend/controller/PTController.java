package com.pbo.backend.controller;

import com.pbo.backend.model.Schedule;
import com.pbo.backend.model.Trainer;
import com.pbo.backend.service.PTRequestService;
import com.pbo.backend.service.ScheduleService;
import com.pbo.backend.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/list")
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "pt/list";
    }

    @GetMapping("/schedule/add")
    public String addScheduleForm() {
        return "pt/add";
    }

    @PostMapping("/schedule/save")
    public String saveSchedule(@ModelAttribute Schedule schedule, Authentication auth) {
        Trainer trainer = trainerService.getTrainerByUsername(auth.getName());
        schedule.setTrainer(trainer);
        schedule.setStatus(Schedule.Status.AVAILABLE);
        scheduleService.saveSchedule(schedule);
        return "redirect:/pt/schedule";
    }

    // UPDATE: Endpoint dengan penanganan Exception melanggar Foreign Key (DataIntegrityViolationException)
    @PostMapping("/schedule/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            scheduleService.deleteSchedule(id);
            redirectAttrs.addFlashAttribute("successMessage", "Jadwal berhasil dihapus.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Menangkap error logika bisnis dari service (misal status sudah dipesan)
            redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // MENANGKAP ERROR FOREIGN KEY: Mencegah crash jika jadwal terikat ke tabel PT_REQUESTS
            redirectAttrs.addFlashAttribute("errorMessage", "Jadwal gagal dihapus karena rekam datanya sedang digunakan dalam riwayat transaksi/permintaan member!");
        } catch (Exception e) {
            // Menangkap error umum lainnya agar sistem tetap aman
            redirectAttrs.addFlashAttribute("errorMessage", "Terjadi kesalahan sistem saat mencoba menghapus jadwal.");
        }
        return "redirect:/pt/schedule";
    }
}