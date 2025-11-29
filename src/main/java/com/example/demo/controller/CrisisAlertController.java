package com.example.demo.controller;

import com.example.demo.dto.crisisAlertDTO;
import com.example.demo.model.CrisisAlert;
import com.example.demo.service.CrisisAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/crisis")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CrisisAlertController {

    private final CrisisAlertService crisisAlertService;

    public CrisisAlertController(CrisisAlertService crisisAlertService) {
        this.crisisAlertService = crisisAlertService;
    }

    // Page view
    @GetMapping
    public String viewAlerts(Model model) {
        model.addAttribute("alerts", crisisAlertService.getUnresolvedAlerts());
        return "admin/crisisAlerts";
    }


    @GetMapping("/api/alerts")
    @ResponseBody
    public List<crisisAlertDTO> getAlerts() {
        return crisisAlertService.getUnresolvedAlerts()
                .stream()
                .map(a -> new crisisAlertDTO(
                        a.getId(),
                        a.getMessage(),
                        a.getCreatedAt().toString(),
                        a.getUser().getUsername(),
                        a.getJournal().getEntryText(),
                        a.isCrisisConfirmed()
                )).toList();
    }


    @PostMapping("/confirm/{id}")
    @ResponseBody
    public ResponseEntity<CrisisAlert> confirmCrisis(@PathVariable Long id, @RequestParam String adminMessage) {
        CrisisAlert alert = crisisAlertService.confirmCrisis(id, adminMessage);
        return ResponseEntity.ok(alert);
    }

//    @PostMapping("/resolve/{id}")
//    @ResponseBody
//    public ResponseEntity<CrisisAlert> resolve(@PathVariable Long id) {
//        CrisisAlert alert = crisisAlertService.resolveAlert(id);
//        return ResponseEntity.ok(alert);
//    }


    @PostMapping("/resolve/{id}")
    @ResponseBody
    public ResponseEntity<?> resolve(@PathVariable Long id) {
        crisisAlertService.markResolved(id); // make sure this method exists
        return ResponseEntity.ok().build();
    }
}