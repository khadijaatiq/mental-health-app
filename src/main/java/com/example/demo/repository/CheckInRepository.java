package com.example.demo.repository;

import com.example.demo.model.CheckIn;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByUser(User user);
    List<CheckIn> findByUserOrderByCreatedAtDesc(User user);

    List<CheckIn> findByUserAndDate(User user, LocalDate date);
    @Query("SELECT c FROM CheckIn c WHERE c.user.id = :userId AND c.createdAt >= :threeDaysAgo")
    List<CheckIn> findRecentCheckIns(@Param("userId") Long userId,
                                     @Param("threeDaysAgo") LocalDateTime threeDaysAgo);
}