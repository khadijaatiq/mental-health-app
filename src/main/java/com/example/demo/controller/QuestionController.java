package com.example.demo.controller;

import com.example.demo.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/questions")
@PreAuthorize("hasRole('ADMIN')")
public class QuestionController {

    @Autowired
    private CheckInService checkInService;

    @GetMapping
    public ResponseEntity<List<String>> getAll() {
        return ResponseEntity.ok(checkInService.getAllQuestions());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, String> body) {
        checkInService.addQuestion(body.get("question"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody Map<String, String> body) {
        checkInService.removeQuestion(body.get("question"));
        return ResponseEntity.ok().build();
    }
}
