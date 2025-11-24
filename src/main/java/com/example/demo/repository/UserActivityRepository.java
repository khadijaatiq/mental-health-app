package com.example.demo.repository;

import com.example.demo.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findTop50ByOrderByTimestampDesc();
    @Query("SELECT DATE(ua.timestamp) as date, ua.action, COUNT(ua) as count " +
            "FROM UserActivity ua " +
            "WHERE ua.timestamp >= :startDate AND ua.timestamp < :endDate " +
            "GROUP BY DATE(ua.timestamp), ua.action " +
            "ORDER BY DATE(ua.timestamp)")
    List<Object[]> getActivityCountsByDateAndAction(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);
}
