package com.example.demo.service;

import com.example.demo.dto.ResourceRequest;
import com.example.demo.model.Resource;
import com.example.demo.model.ResourceCategory;
import com.example.demo.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository repository;
    private final ResourceCategoryService categoryService;

    public ResourceService(ResourceRepository repository, ResourceCategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    /**
     * Create resource from DTO (category name resolved/created)
     */
    public Resource createResource(ResourceRequest req) {
        ResourceCategory cat = categoryService.getOrCreate(req.getCategory());
        Resource resource = new Resource(req.getTitle(), req.getDescription(), cat, req.getLink(), req.getFileUrl());
        return repository.save(resource);
    }


    public Resource createResource(Resource resource) {
        if (resource.getCategory() == null) {
            resource.setCategory(categoryService.getOrCreate("General"));
        } else {
            resource.setCategory(categoryService.getOrCreate(resource.getCategory().getName()));
        }
        return repository.save(resource);
    }

    public List<Resource> getAllResources() {
        return repository.findAll();
    }

    public Resource getResource(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found: " + id));
    }


    public Resource updateResource(Long id, ResourceRequest req) {
        Resource existing = getResource(id);
        ResourceCategory cat = categoryService.getOrCreate(req.getCategory());
        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setCategory(cat);
        existing.setLink(req.getLink());
        existing.setFileUrl(req.getFileUrl());
        return repository.save(existing);
    }


    public void deleteResource(Long id) {
        repository.deleteById(id);
    }

    public List<Resource> searchResources(String keyword) {
        return repository.smartSearch(keyword);
    }
    public List<Resource> searchWithCategory(String keyword, String category) {
        return repository.findByTitleContainingIgnoreCaseAndCategory_NameIgnoreCase(keyword, category);
    }

    public List<Resource> filterByCategory(String categoryName) {
        return repository.findByCategory_Name(categoryName);
    }
}