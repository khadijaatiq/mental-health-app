package com.example.demo.repository;

import com.example.demo.model.Stress;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StressRepository extends JpaRepository<Stress, Long> {
    List<Stress> findByUser(User user);
    List<Stress> findByUserOrderByDateDesc(User user);
    List<Stress> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    @Query("SELECT AVG(s.stressLevel) FROM Stress s WHERE s.user = :user AND s.date BETWEEN :start AND :end")
    Double getAverageStressLevel(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
}