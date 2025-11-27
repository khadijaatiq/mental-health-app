package com.example.demo.controller;

import com.example.demo.dto.ResourceRequest;
import com.example.demo.model.Resource;
import com.example.demo.model.ResourceCategory;
import com.example.demo.service.ResourceCategoryService;
import com.example.demo.service.ResourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ResourceCategoryService categoryService;

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    public ResourceController(ResourceService resourceService, ResourceCategoryService categoryService) {
        this.resourceService = resourceService;
        this.categoryService = categoryService;
    }

    // CREATE (Admin only) - accepts ResourceRequest
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Resource create(@RequestBody ResourceRequest req) {
        return resourceService.createResource(req);
    }

    // Create category (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public ResourceCategory createCategory(@RequestBody ResourceCategory payload) {
        return categoryService.getOrCreate(payload.getName());
    }

    // READ ALL (Public)
    @GetMapping
    public List<Resource> all() {
        return resourceService.getAllResources();
    }

    // READ ONE (Public)
    @GetMapping("/{id}")
    public Resource get(@PathVariable Long id) {
        return resourceService.getResource(id);
    }

    // UPDATE (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Resource update(@PathVariable Long id, @RequestBody ResourceRequest req) {
        return resourceService.updateResource(id, req);
    }

    // DELETE (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        resourceService.deleteResource(id);
    }

    // SEARCH (Public)
    @GetMapping("/search")
    public List<Resource> search(@RequestParam String keyword) {
        return resourceService.searchResources(keyword);
    }

    // FILTER BY CATEGORY (Public)
    @GetMapping("/category/{category}")
    public List<Resource> filter(@PathVariable String category) {
        return resourceService.filterByCategory(category);
    }

    // FILE UPLOAD (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // Return the file path (relative)
        return uploadDir + filename;
    }

    // LOAD DEFAULT RESOURCES (Admin only - convenience endpoint for demo)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/load-defaults")
    public List<Resource> loadDefaults() {
        // create default categories via categoryService.getOrCreate
        ResourceCategory med = categoryService.getOrCreate("Meditation");
        ResourceCategory stress = categoryService.getOrCreate("Stress Management");
        ResourceCategory dep = categoryService.getOrCreate("Depression Support");
        ResourceCategory mind = categoryService.getOrCreate("Mindfulness");
        ResourceCategory anx = categoryService.getOrCreate("Anxiety Relief");

        List<Resource> defaultResources = List.of(
                new Resource(
                        "Meditation Guide",
                        "Comprehensive guide to meditation techniques for beginners and advanced practitioners. Learn mindfulness meditation, breathing exercises, and body scan techniques.",
                        med,
                        "https://www.mindful.org/meditation/mindfulness-getting-started/",
                        null),
                new Resource(
                        "Stress Management Toolkit",
                        "Evidence-based strategies for managing stress in daily life. Includes practical exercises, coping mechanisms, and relaxation techniques.",
                        stress,
                        "https://www.apa.org/topics/stress/tips",
                        null),
                new Resource(
                        "Understanding Depression",
                        "Educational resource about depression, its symptoms, causes, and treatment options. Includes information about therapy and medication.",
                        dep,
                        "https://www.nimh.nih.gov/health/topics/depression",
                        null),
                new Resource(
                        "Mindfulness Exercises",
                        "Daily mindfulness practices for mental wellness. Simple exercises you can do anywhere to reduce anxiety and improve focus.",
                        mind,
                        "https://www.headspace.com/mindfulness",
                        null),
                new Resource(
                        "Anxiety Coping Strategies",
                        "Practical techniques for managing anxiety and panic attacks. Learn grounding exercises, cognitive strategies, and breathing techniques.",
                        anx,
                        "https://www.anxietycanada.com/resources/",
                        null));

        return defaultResources.stream()
                .map(resourceService::createResource)
                .toList();
    }

    // CATEGORIES endpoint (Public) - list of category names
    @GetMapping("/categories")
    public List<String> categories() {
        // prefer fetching category table names
        return categoryService.getAll().stream().map(ResourceCategory::getName).toList();
    }

    @GetMapping("/filter")
    public List<Resource> filterCombined(
            @RequestParam String keyword,
            @RequestParam String category) {
        return resourceService.searchWithCategory(keyword, category);
    }

    // Delete category by name (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories/{name}")
    public void deleteCategory(@PathVariable String name) {
        // NOTE: In production you'd want to reassign or prevent deletion if resources exist.
        categoryService.deleteByName(name);
    }
}