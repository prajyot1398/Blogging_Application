package com.bloggingapi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bloggingapi.blogenum.PostAttrsEnum;
import com.bloggingapi.payload.CategoryForm;
import com.bloggingapi.payload.CommentForm;
import com.bloggingapi.payload.PostForm;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.multi.PaginationWithContent;

public interface PostService {
	
	//Create
	PostForm createPost(PostForm postForm, MultipartFile postImageFile);
	
	//Update
	PostForm updatePost(PostForm postForm, String postAttrValue, PostAttrsEnum postAttr, MultipartFile postImageFile);
	
	//Get All
	PaginationWithContent<List<PostForm>> getAllPosts(Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc);
	
	//Delete
	void deletePost(String postAttrValue, PostAttrsEnum postAttr);
	
	//Get By PostAttr;
	PostForm getPostByAttr(String postAttrValue, PostAttrsEnum postAttr);
	
	//Get Posts By User
	PaginationWithContent<List<PostForm>> getPostsByUser(UserForm userForm,  String userAttr, String userAttrValue, 
			Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc);
	
	//Get Posts By Category
	PaginationWithContent<List<PostForm>> getPostsByCategory(CategoryForm categoryForm, String categoryAttr, String categoryAttrValue,
			Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc);
	
	//Get Posts By Keyword
	PaginationWithContent<List<PostForm>> searchPostByKeyword(String keyWord,
			Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc);

	PostForm addOrDeleteComment(CommentForm commentForm, boolean delete);
}
