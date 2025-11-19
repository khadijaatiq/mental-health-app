package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Post> getFlaggedPosts() {
        return postRepository.findByFlagged(true);
    }

    public Post flagPost(Long id, String reason) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setFlagged(true);
            post.setFlagReason(reason);
            return postRepository.save(post);
        }
        return null;
    }

    public Post unflagPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setFlagged(false);
            post.setFlagReason(null);
            return postRepository.save(post);
        }
        return null;
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}