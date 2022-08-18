package com.bloggingapi.util;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.bloggingapi.entity.Comment;
import com.bloggingapi.entity.Post;
import com.bloggingapi.payload.CommentForm;
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
	
	public static Comment commentFormToComment(CommentForm form) {
		
		Comment comment = new ModelMapper().map(form, Comment.class);
		return comment;
	}
	
	public static CommentForm commentToCommentForm(Comment comment) {
		 
		CommentForm form = new ModelMapper().map(comment, CommentForm.class);
		updateNullValuesInCommentFormFromComment(form, comment);
		return form;
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
		/*
		// Commented because, in case only imageName is received and no file from client
		// then it should not override the existing name.  
		if (post.getPostImage() == null && form.getPostImage() != null) {
			post.setPostImage(form.getPostImage());
		}*/ 
	}
	
	public static void updateNullValuesInCommentFromCommentForm(CommentForm form, Comment comment) {
		
		if(comment.getCommentId() == null && form.getCommentId() != null) {
			comment.setCommentId(form.getCommentId());
		}
		if(comment.getComment() == null) {
			comment.setComment(form.getComment());
		}
		if(comment.getAddedDate() == null) {
			if(form.getAddedDate() != null) {
				comment.setAddedDate(form.getAddedDate());
			}
			else {
				comment.setAddedDate(new Date());
			}
		}
	}
	
	public static void updateNullValuesInCommentFormFromComment(CommentForm form, Comment comment) {
		
		if(form.getCommentId() == null && comment.getCommentId() != null) {
			form.setCommentId(comment.getCommentId());
		}
		if(form.getComment() == null) {
			form.setComment(comment.getComment());
		}
		if(form.getAddedDate() == null) {
			form.setAddedDate(comment.getAddedDate());
		}
		if(form.getPostTitle() == null) {
			form.setPostTitle(comment.getPost().getPostTitle());
		}
		if(form.getUserEmail() == null) {
			form.setUserEmail(comment.getUser().getUserEmail());
		}
	}
 }
