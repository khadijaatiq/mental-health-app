package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.CrisisAlertService;
import com.example.demo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crisis")
public class UserAlertController {

    private final CrisisAlertService crisisAlertService;

    public UserAlertController(CrisisAlertService crisisAlertService) {
        this.crisisAlertService = crisisAlertService;
    }

    @GetMapping
    public String viewUserAlerts(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("alerts", crisisAlertService.getUserAlerts(user));
        return "/crisis";
    }
}