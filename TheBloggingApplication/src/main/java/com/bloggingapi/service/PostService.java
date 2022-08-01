package com.bloggingapi.service;

import java.util.List;


import com.bloggingapi.blogenum.PostAttrsEnum;
import com.bloggingapi.payload.CategoryForm;
import com.bloggingapi.payload.PostForm;
import com.bloggingapi.payload.UserForm;

public interface PostService {
	
	//Create
	PostForm createPost(PostForm postForm);
	
	//Update
	PostForm updatePost(PostForm postForm, String postAttrValue, PostAttrsEnum postAttr);
	
	//Get All
	List<PostForm> getAllPosts();
	
	//Delete
	void deletePost(String postAttrValue, PostAttrsEnum postAttr);
	
	//Get By PostAttr;
	PostForm getPostByAttr(String postAttrValue, PostAttrsEnum postAttr);
	
	//Get Posts By User
	List<PostForm> getPostsByUser(UserForm userForm,  String userAttr, String userAttrValue);
	
	//Get Posts By Category
	List<PostForm> getPostsByCategory(CategoryForm categoryForm, String categoryAttr, String categoryAttrValue);
	
	//Get Posts By Keyword
	List<PostForm> searchPostByKeyword(String keyWord);
}
