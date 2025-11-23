package com.example.demo.repository;

import com.example.demo.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // find resources by category name (category.name)
    List<Resource> findByCategory_Name(String categoryName);

    List<Resource> findByTitleContainingIgnoreCase(String keyword);
    @Modifying
    @Query("UPDATE Resource r SET r.category = NULL WHERE r.category.id = :catId")
    void removeCategoryFromResources(Long catId);

    List<Resource> findByCategory_NameAndTitleContainingIgnoreCase(String categoryName, String keyword);

    // distinct category names (for dropdown)
    @Query("SELECT DISTINCT r.category.name FROM Resource r WHERE r.category IS NOT NULL")
    List<String> findDistinctCategoryNames();
}
