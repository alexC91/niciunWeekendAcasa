package com.services;

import com.linkDatabase.Post;
import com.linkDatabase.Users;
import com.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(String content, Users user) {
        System.out.println("Creating post for user: " + user.getEmail());
        System.out.println("Post content: " + content);

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setLikesCount(0);
        post.setSharesCount(0);

        Post savedPost = postRepository.save(post);
        System.out.println("Post saved with ID: " + savedPost.getId());

        return savedPost;
    }

    public List<Post> getUserPosts(Users user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post likePost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setLikesCount(post.getLikesCount() + 1);
            return postRepository.save(post);
        }
        return null;
    }

    public Post sharePost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setSharesCount(post.getSharesCount() + 1);
            return postRepository.save(post);
        }
        return null;
    }
}
