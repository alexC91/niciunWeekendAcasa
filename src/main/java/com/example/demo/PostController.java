package com.example.demo;

import com.linkDatabase.Comment;
import com.linkDatabase.Post;
import com.linkDatabase.Users;
import com.repositories.UserRepository;
import com.services.CommentService;
import com.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create-post")
    @ResponseBody
    public ResponseEntity<?> createPost(@RequestParam("content") String content) {
        try {
            System.out.println("Received post creation request with content: " + content);

            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            System.out.println("Current user email: " + email);

            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                System.out.println("Found user: " + user.getFirstName() + " " + user.getLastName());

                Post post = postService.createPost(content, user);
                System.out.println("Post created with ID: " + post.getId());

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("postId", post.getId());
                response.put("userName", user.getFirstName() + " " + user.getLastName());
                response.put("userImage", user.getProfilePhoto());
                response.put("content", post.getContent());
                response.put("createdAt", post.getCreatedAt());

                return ResponseEntity.ok(response);
            } else {
                System.out.println("User not found for email: " + email);
                return ResponseEntity.badRequest().body("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating post: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error creating post: " + e.getMessage());
        }
    }

    @PostMapping("/create-comment")
    @ResponseBody
    public ResponseEntity<?> createComment(
            @RequestParam("content") String content,
            @RequestParam("postId") Long postId) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Optional<Users> userOptional = userRepository.findByEmail(email);
            Optional<Post> postOptional = postService.getPostById(postId);

            if (userOptional.isPresent() && postOptional.isPresent()) {
                Users user = userOptional.get();
                Post post = postOptional.get();

                Comment comment = commentService.createComment(content, user, post);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("commentId", comment.getId());
                response.put("userName", user.getFirstName() + " " + user.getLastName());
                response.put("userImage", user.getProfilePhoto());
                response.put("content", comment.getContent());
                response.put("createdAt", comment.getCreatedAt());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("User or post not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating comment: " + e.getMessage());
        }
    }

    @PostMapping("/like-post")
    @ResponseBody
    public ResponseEntity<?> likePost(@RequestParam("postId") Long postId) {
        try {
            Post post = postService.likePost(postId);
            if (post != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("likesCount", post.getLikesCount());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Post not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error liking post: " + e.getMessage());
        }
    }

    @PostMapping("/share-post")
    @ResponseBody
    public ResponseEntity<?> sharePost(@RequestParam("postId") Long postId) {
        try {
            Post post = postService.sharePost(postId);
            if (post != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("sharesCount", post.getSharesCount());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Post not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sharing post: " + e.getMessage());
        }
    }

    @GetMapping("/get-post-comments")
    @ResponseBody
    public ResponseEntity<?> getPostComments(@RequestParam("postId") Long postId) {
        try {
            Optional<Post> postOptional = postService.getPostById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                List<Comment> comments = commentService.getPostComments(post);
                return ResponseEntity.ok(comments);
            } else {
                return ResponseEntity.badRequest().body("Post not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting comments: " + e.getMessage());
        }
    }
}
