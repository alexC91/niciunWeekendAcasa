package com.repositories;

import com.linkDatabase.Post;
import com.linkDatabase.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByCreatedAtDesc(Users user);
    List<Post> findAllByOrderByCreatedAtDesc();
}
