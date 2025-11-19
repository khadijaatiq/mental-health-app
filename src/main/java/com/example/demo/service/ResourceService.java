package com.example.demo.service;

import com.example.demo.model.Resource;
import com.example.demo.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resource getResource(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    public List<Resource> getResourcesByTag(String tag) {
        return resourceRepository.findByTag(tag);
    }

    public List<Resource> searchResources(String keyword) {
        return resourceRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Resource> getAdminResources() {
        return resourceRepository.findByAddedByAdmin(true);
    }

    public Resource updateResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}