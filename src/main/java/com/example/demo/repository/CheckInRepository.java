package com.example.demo.repository;

import com.example.demo.model.CheckIn;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByUser(User user);
    List<CheckIn> findByUserOrderByCreatedAtDesc(User user);
}