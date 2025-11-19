package com.example.demo.controller;

import com.example.demo.dto.PostDTO;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody PostDTO postDTO, @AuthenticationPrincipal User user) {
        Post post = new Post();
        post.setUser(user);
        post.setContent(postDTO.getContent());
        post.setAnonymous(postDTO.isAnonymous());

        Post saved = postService.createPost(post);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(@AuthenticationPrincipal User user) {
        List<Post> posts = postService.getAllPosts();

        // Convert to DTOs, hiding username for anonymous posts
        List<PostDTO> postDTOs = posts.stream()
                .filter(p -> !p.isFlagged()) // Don't show flagged posts to regular users
                .map(p -> new PostDTO(
                        p.getId(),
                        p.getContent(),
                        p.isAnonymous(),
                        p.getDate(),
                        p.isFlagged(),
                        p.getFlagReason(),
                        p.isAnonymous() ? "Anonymous" : p.getUser().getUsername()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<Post>> getMyPosts(@AuthenticationPrincipal User user) {
        List<Post> posts = postService.getPostsByUser(user);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        Post post = postService.getPost(id);
        if (post != null && !post.isFlagged()) {
            PostDTO postDTO = new PostDTO(
                    post.getId(),
                    post.getContent(),
                    post.isAnonymous(),
                    post.getDate(),
                    post.isFlagged(),
                    post.getFlagReason(),
                    post.isAnonymous() ? "Anonymous" : post.getUser().getUsername()
            );
            return ResponseEntity.ok(postDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody PostDTO postDTO, @AuthenticationPrincipal User user) {
        Post post = postService.getPost(id);
        if (post != null && post.getUser().getId().equals(user.getId())) {
            post.setContent(postDTO.getContent());
            post.setAnonymous(postDTO.isAnonymous());
            Post updated = postService.updatePost(post);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Post post = postService.getPost(id);
        if (post != null && post.getUser().getId().equals(user.getId())) {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}