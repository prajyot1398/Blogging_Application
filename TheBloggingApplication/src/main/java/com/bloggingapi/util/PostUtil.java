package com.bloggingapi.util;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.bloggingapi.entity.Post;
import com.bloggingapi.payload.PostForm;

public class PostUtil {
	
	
	public static Post postFormToPost(PostForm postForm) {
		
		Post post = new ModelMapper().map(postForm, Post.class);
		return post;
	}
	
	public static PostForm postToPostForm(Post post) {
		
		PostForm postForm = new ModelMapper().map(post, PostForm.class);
		updateNullValuesInPostFormFromPost(postForm, post);
		return postForm;
	}
	
	public static List<PostForm> getPostFormListFromPostList(List<Post> postList) {
		
		List<PostForm> formList = null;
		if(postList != null && postList.size() > 0) {
			formList = postList.stream().map((post) -> postToPostForm(post)).collect(Collectors.toList());
		}
		return formList;
	}
	
	public static List<Post> getPostListFromPostFormList(List<PostForm> formList) {
		
		List<Post> postList = null;
		if(formList != null && formList.size() > 0) {
			postList = formList.stream().map((postForm) -> postFormToPost(postForm)).collect(Collectors.toList());
		}
		return postList;
	}
	
	public static void updateNullValuesInPostFormFromPost(PostForm form, Post post) {
		
		if(form.getPostId() == null) {
			form.setPostId(post.getPostId());
		}
		if(form.getPostTitle() == null) {
			form.setPostTitle(post.getPostTitle());
		}
		if(form.getPostContent() == null) {
			form.setPostContent(post.getPostContent());
		}
		if(form.getPostAddedDate() == null) {
			form.setPostAddedDate(post.getAddedDate());
		}
		if(form.getPostImage() == null && post.getPostImage() != null) {
			form.setPostImage(post.getPostImage());
		}
		if(form.getUserEmail() == null && post.getUser() != null) {
			form.setUserEmail(post.getUser().getUserEmail());
		}
		if(form.getCategoryName() == null && post.getCategory() != null) {
			form.setCategoryName(post.getCategory().getCategoryName());
		}
	}
	
	public static void updateNullValuesInPostFromPostForm(PostForm form, Post post) {
		
		if(post.getPostId() == null && form.getPostId() != null) {
			post.setPostId(form.getPostId());
		}
		if(post.getPostTitle() == null) {
			post.setPostTitle(form.getPostTitle());
		}
		if(post.getPostContent() == null) {
			post.setPostContent(form.getPostContent());
		}
		if(post.getAddedDate() == null) {
			if(form.getPostAddedDate() != null)
				post.setAddedDate(form.getPostAddedDate());
			else 
				post.setAddedDate(new Date());
		}
		if(post.getPostImage() == null) {
			if(form.getPostImage() != null)
				post.setPostImage(form.getPostImage());
			else 
				post.setPostImage("default.png");
		}
	}
 }
