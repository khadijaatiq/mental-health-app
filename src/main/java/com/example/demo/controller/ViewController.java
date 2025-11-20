package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/mood")
    public String mood() {
        return "mood";
    }

    @GetMapping("/journal")
    public String journal() {
        return "journal";
    }

    @GetMapping("/goals")
    public String goals() {
        return "goals";
    }

    @GetMapping("/habits")
    public String habits() {
        return "habit";
    }

    @GetMapping("/stress")
    public String stress() {
        return "stress";
    }

    @GetMapping("/checkin")
    public String checkin() {
        return "checkin";
    }

    @GetMapping("/board")
    public String board() {
        return "board";
    }

    @GetMapping("/resources-page")
    public String resources() {
        return "resources";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "analytics";
    }

    @GetMapping("/export")
    public String export() {
        return "export";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}
