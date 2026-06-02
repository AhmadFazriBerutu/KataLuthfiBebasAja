package com.pbo.backend.controller;

import com.pbo.backend.service.KunjunganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/kunjungan")
public class KunjunganController {

    @Autowired
    private KunjunganService kunjunganService;

    @GetMapping
    public String index(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggal,
            Model model) {

        if (tanggal == null) tanggal = LocalDate.now();

        model.addAttribute("kunjunganList", kunjunganService.getKunjunganByTanggal(tanggal));
        model.addAttribute("tanggalFilter", tanggal);
        model.addAttribute("totalHariIni", kunjunganService.countHariIni());
        return "admin/kunjungan/index";
    }

    @PostMapping("/catat")
    public String catat(@RequestParam String namaKunjungan,
                        @RequestParam(required = false) String keterangan) {
        kunjunganService.catat(namaKunjungan, keterangan);
        return "redirect:/admin/kunjungan";
    }

    @PostMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        kunjunganService.hapus(id);
        return "redirect:/admin/kunjungan";
    }
}