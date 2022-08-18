package com.bloggingapi.repository;


import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloggingapi.entity.Comment;
import com.bloggingapi.entity.Post;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
	
	Set<Comment> findByPost(Post post);
}
