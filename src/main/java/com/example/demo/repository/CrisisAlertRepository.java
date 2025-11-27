package com.example.demo.repository;

import com.example.demo.model.CrisisAlert;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrisisAlertRepository extends JpaRepository<CrisisAlert, Long> {

    List<CrisisAlert> findByReviewedFalse();     // Admin sees unreviewed alerts

    List<CrisisAlert> findByUserOrderByCreatedAtDesc(User user);  // User view

    List<CrisisAlert> findByCrisisConfirmedTrueAndResolvedFalse(); // Active crises

    List<CrisisAlert> findByResolvedFalse();

}