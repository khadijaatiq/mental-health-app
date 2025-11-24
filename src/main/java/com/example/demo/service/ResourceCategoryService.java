package com.example.demo.service;

import com.example.demo.model.ResourceCategory;
import com.example.demo.repository.ResourceCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceCategoryService {

    private final ResourceCategoryRepository repository;

    public ResourceCategoryService(ResourceCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ResourceCategory> getAll() {
        return repository.findAll();
    }

    public ResourceCategory getOrCreate(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "General";
        }
        String trimmed = name.trim();
        return repository.findByNameIgnoreCase(trimmed)
                .orElseGet(() -> repository.save(new ResourceCategory(trimmed)));
    }

    public ResourceCategory findByName(String name) {
        return repository.findByNameIgnoreCase(name).orElse(null);
    }
    @Transactional
    public void deleteByName(String name) {
        ResourceCategory cat = repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if any resources use this category
        if (!cat.getResources().isEmpty()) {
            throw new RuntimeException("Cannot delete category - resources are using it");
        }

        repository.delete(cat);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }


    public boolean exists(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    public List<String> getDistinctCategoryNamesFromResources(com.example.demo.repository.ResourceRepository resourceRepository) {
        return resourceRepository.findDistinctCategoryNames();
    }
}
