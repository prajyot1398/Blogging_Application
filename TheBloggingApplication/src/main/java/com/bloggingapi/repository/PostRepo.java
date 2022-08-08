package com.bloggingapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bloggingapi.entity.Category;
import com.bloggingapi.entity.Post;
import com.bloggingapi.entity.User;

public interface PostRepo extends JpaRepository<Post, Integer> {
	
	Optional<Post> findByPostTitle(String postTitle);
	
	boolean existsPostByPostTitle(String postTitle);

	Page<Post> findByCategory(Category category, Pageable pagination);

	Page<Post> findByUser(User user, Pageable pagination);
}
