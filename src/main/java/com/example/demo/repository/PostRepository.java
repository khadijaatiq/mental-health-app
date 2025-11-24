package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByFlagged(boolean flagged);
    List<Post> findByAnonymous(boolean anonymous);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUserOrderByCreatedAtDesc(User user);
    List<Post> findByCreatedAtAfter(LocalDateTime date);
}