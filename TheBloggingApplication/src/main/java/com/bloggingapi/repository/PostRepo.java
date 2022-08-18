package com.bloggingapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bloggingapi.entity.Category;
import com.bloggingapi.entity.Post;
import com.bloggingapi.entity.User;

public interface PostRepo extends JpaRepository<Post, Integer> {
	
	Optional<Post> findByPostTitle(String postTitle);
	
	boolean existsPostByPostTitle(String postTitle);

	Page<Post> findByCategory(Category category, Pageable pagination);

	Page<Post> findByUser(User user, Pageable pagination);
	
	/*
	 * Used when we have to use manual query. Insetead of genarated query.
	 * And use %keyword% while passing the value
	 * */
	//@Query("SELECT P FROM Post P WHERE P.postTitle like :key")
	//Page<Post> searchByPostTitleContaining(@Param("key") String keyword, Pageable pagination);
	
	Page<Post> findByPostTitleContaining(String keyword, Pageable pagination);
}
