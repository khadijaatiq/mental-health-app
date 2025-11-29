package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final UserActivityService userActivityService;

    public PostService(PostRepository postRepository, NotificationService notificationService,
                       UserActivityService userActivityService) {
        this.postRepository = postRepository;
        this.notificationService = notificationService;
        this.userActivityService = userActivityService;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByDeletedFalseOrderByCreatedAtDesc();
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

    @Transactional
    public void deletePostAsAdmin(Long postId, User admin, String reason) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User owner = post.getUser();

        // SOFT-DELETE
        post.setDeleted(true);
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);

        // Send notification via NotificationService
        String message = String.format("Your post (id=%d) was removed by an administrator. Reason: %s", postId, reason);
        notificationService.createAndDeliver(
                owner.getId(),
                "ADMIN_ACTION",
                message,
                null,
                true,
                owner.getEmail()
        );

        // Log admin action if needed
        if (admin != null && userActivityService != null) {
            userActivityService.logActivity(admin, "POST_DELETED_BY_ADMIN", "Deleted post ID: " + postId + " Reason: " + reason);
        }
    }


    /**
     * Admin deletes a post and sends a notification to the post owner with the admin's reason.
     *
     * @param postId id of the post to delete
     * @param admin  the admin user performing deletion (optional - may be null)
     * @param reason reason to send to the user (displayed to user)
     * @throws IllegalArgumentException if post not found
     */
//    @Transactional
//    public void deletePostAsAdmin(Long postId, User admin, String reason) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
//
//        User owner = post.getUser();
//
//        // Delete the post
//        postRepository.delete(post);
//
//        // Send notification via NotificationService
//        String message = String.format("Your post (id=%d) was removed by an administrator. Reason: %s", postId, reason);
//        notificationService.createAndDeliver(
//                owner.getId(),
//                "FLAG",          // type can be FLAG or ADMIN_ACTION
//                message,
//                null,            // no link in this example, or you can add one to the post list page
//                true,            // send email if offline
//                owner.getEmail() // email address
//        );
//
//        // Log admin action if needed
//        if (admin != null && userActivityService != null) {
//            userActivityService.logActivity(admin, "POST_DELETED_BY_ADMIN", "Deleted post ID: " + postId + " Reason: " + reason);
//        }
//    }

}
//remove useractivity service if it doesnt look good