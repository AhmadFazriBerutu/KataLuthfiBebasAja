package com.pbo.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/members")
    public String members() {
        return "member/list";
    }

    @GetMapping("/members/add")
    public String addMember() {
        return "member/add";
    }

    @GetMapping("/trainers")
    public String trainers() {
        return "trainer/list";
    }

    @GetMapping("/trainers/add")
    public String addTrainer() {
        return "trainer/add";
    }
}