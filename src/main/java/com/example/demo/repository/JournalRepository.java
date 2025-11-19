package com.example.demo.repository;

import com.example.demo.model.Journal;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByUser(User user);
    List<Journal> findByUserOrderByDateDesc(User user);
    Optional<Journal> findByUserAndDate(User user, LocalDate date);
    List<Journal> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Journal> findByUserAndEmotionTag(User user, String emotionTag);

    @Query("SELECT j.emotionTag, COUNT(j) FROM Journal j WHERE j.user = :user GROUP BY j.emotionTag")
    List<Object[]> countEmotionsByUser(@Param("user") User user);
}