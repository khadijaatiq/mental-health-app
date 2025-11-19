package com.example.demo.repository;

import com.example.demo.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByTag(String tag);
    List<Resource> findByAddedByAdmin(boolean addedByAdmin);
    List<Resource> findByTitleContainingIgnoreCase(String keyword);
}