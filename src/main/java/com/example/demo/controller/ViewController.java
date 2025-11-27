package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ViewController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String userDashboard(Authentication auth, Model model) {

        if (auth == null) {
            return "redirect:/login";
        }

        User user = (User) auth.getPrincipal();

        boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("isAdmin", false);
        return "dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication auth) {
        if (auth == null)
            return "redirect:/login";

        User user = (User) auth.getPrincipal();
        if (!user.getRoles().contains("ROLE_ADMIN")) {
            return "redirect:/dashboard";
        }

        return "adminDashboard";
    }

    @GetMapping("/resources")
    public String resources() {
        return "resources"; // Points to resources.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Points to register.html
    }

    // user
    @GetMapping("/mood")
    public String moodPage() {
        return "mood";
    }

    // @GetMapping("/habit")
    // public String habitPage() {
    // return "habit";
    // }
    @GetMapping("/journal")
    public String journalPage() {
        return "journal";
    }

    @GetMapping("/goals")
    public String goalsPage() {
        return "goals";
    }

    @GetMapping("/habits")
    public String habitsPage() {
        return "habit";
    }

    @GetMapping("/stress")
    public String stressPage() {
        return "stress";
    }

    @GetMapping("/checkin")
    public String checkinPage() {
        return "checkin";
    }

    @GetMapping("/board")
    public String boardPage() {
        return "board";
    }

    // @GetMapping("/resources")
    // public String resourcesPage() {
    // return "resources";
    // }

    @GetMapping("/analytics")
    public String analyticsPage() {
        return "analytics";
    }

    @GetMapping("/export")
    public String exportPage() {
        return "export";
    }

    // admin
    @GetMapping("/admin/resources")
    public String adminResources() {
        return "admin/resources";
    }

    @GetMapping("/admin/board")
    public String adminBoard() {
        return "admin/board";
    }
//
//    @GetMapping("/admin/crisis")
//    public String adminCrisis() {
//        return "admin/crisis";
//    }

    @GetMapping("/admin/reports")
    public String adminReports() {
        return "admin/reports";
    }

    @GetMapping("/admin/settings")
    public String adminSettings() {
        return "admin/settings";
    }

    @GetMapping("/admin/users")
    public String adminUsers() {
        return "admin/users";
    }
}
