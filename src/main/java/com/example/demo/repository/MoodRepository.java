package com.example.demo.repository;

import com.example.demo.model.Mood;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Long> {
    List<Mood> findByUser(User user);

    List<Mood> findByUserOrderByDateDesc(User user);

    Optional<Mood> findByUserAndDate(User user, LocalDateTime date);

    List<Mood> findByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(m.intensity) FROM Mood m WHERE m.user = :user AND m.date BETWEEN :start AND :end")
    Double getAverageIntensity(@Param("user") User user, @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT m.moodLevel, COUNT(m) FROM Mood m WHERE m.user = :user GROUP BY m.moodLevel")
    List<Object[]> countMoodsByUser(@Param("user") User user);
}