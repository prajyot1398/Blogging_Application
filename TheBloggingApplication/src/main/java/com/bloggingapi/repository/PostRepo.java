package com.bloggingapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloggingapi.entity.Post;

public interface PostRepo extends JpaRepository<Post, Integer> {
	
	Optional<Post> findByPostTitle(String postTitle);
	
	boolean existsPostByPostTitle(String postTitle);
}
