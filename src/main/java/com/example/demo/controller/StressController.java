package com.example.demo.controller;

import com.example.demo.model.Stress;
import com.example.demo.model.User;
import com.example.demo.service.StressService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stress")
public class StressController {

    private final StressService stressService;

    public StressController(StressService stressService) {
        this.stressService = stressService;
    }

    @PostMapping
    public ResponseEntity<Stress> create(@RequestBody Stress stress, @AuthenticationPrincipal User user) {
        stress.setUser(user);
        Stress saved = stressService.createStress(stress);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Stress>> getUserStress(@AuthenticationPrincipal User user) {
        List<Stress> stressList = stressService.getStressByUser(user);
        return ResponseEntity.ok(stressList);
    }

    @GetMapping("/range")
    public ResponseEntity<List<Stress>> getStressByDateRange(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Stress> stressList = stressService.getStressByUserAndDateRange(user, start, end);
        return ResponseEntity.ok(stressList);
    }

    @GetMapping("/average")
    public ResponseEntity<Map<String, Double>> getAverageStress(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Double avg = stressService.getAverageStressLevel(user, start, end);
        return ResponseEntity.ok(Map.of("averageStressLevel", avg != null ? avg : 0.0));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stress> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Stress stress = stressService.getStress(id);
        if (stress != null && stress.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(stress);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stress> update(@PathVariable Long id, @RequestBody Stress stressUpdate, @AuthenticationPrincipal User user) {
        Stress stress = stressService.getStress(id);
        if (stress != null && stress.getUser().getId().equals(user.getId())) {
            stress.setStressLevel(stressUpdate.getStressLevel());
            stress.setNotes(stressUpdate.getNotes());
            Stress updated = stressService.updateStress(stress);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Stress stress = stressService.getStress(id);
        if (stress != null && stress.getUser().getId().equals(user.getId())) {
            stressService.deleteStress(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}