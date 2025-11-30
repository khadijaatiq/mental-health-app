package com.example.demo.repository;

import com.example.demo.model.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Long> {

    Optional<ResourceCategory> findByNameIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM ResourceCategory c WHERE LOWER(c.name) = LOWER(:name)")
    void deleteByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

}
