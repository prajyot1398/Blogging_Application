package com.bloggingapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bloggingapi.blogenum.PostAttrsEnum;
import com.bloggingapi.exception.InvalidFieldException;
import com.bloggingapi.payload.PostForm;
import com.bloggingapi.payload.apiresponse.ApiResponse;
import com.bloggingapi.payload.apiresponse.ApiResponseWithObject;
import com.bloggingapi.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	//POST : Create
	@PostMapping("/")
	public ResponseEntity<ApiResponse> createPost(@Valid @RequestBody PostForm postForm) {
		
		String responseMessage = null;
		if(postForm.getPostId() != null) {
			responseMessage = "You have provided postId in POST Request, it might "
					+ "override existing post if present with same id. It won't consider"
					+ " postId if post not already exists as ids are auto-incremented";
		}else {
			responseMessage = "Post created successfully !!"; 
		}
		
		postForm = this.postService.createPost(postForm);
		
		return new ResponseEntity<ApiResponse>(new ApiResponseWithObject(responseMessage, true, postForm), HttpStatus.CREATED);
	}
	
	//GET :- Get All The Categories
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getAllPosts() {
		
		List<PostForm> postList = this.postService.getAllPosts();
		return new ResponseEntity<ApiResponse>(new ApiResponseWithObject("List Of Posts.", true, postList), HttpStatus.OK);
	}
	
	//GET :- Get Post Based On Attr
	@GetMapping("/{postAttr}/{postAttrValue}")
	public ResponseEntity<ApiResponse> getPost(@PathVariable("postAttr") String postAttr,
			@PathVariable("postAttrValue") String postAttrValue) {
		PostAttrsEnum attr = PostAttrsEnum.POST_ID;
		if(postAttr.equals("id")) {
			attr = PostAttrsEnum.POST_ID;
		} else if(postAttr.equals("title")) {
			attr = PostAttrsEnum.POST_TITLE;
		} else {
			throw new InvalidFieldException(postAttr, "Post", List.of("id", "title"));
		}
		PostForm postForm = this.postService.getPostByAttr(postAttrValue, attr);
		return new ResponseEntity<ApiResponse>(new ApiResponseWithObject(
				"Post with "+postAttr+" : "+postAttrValue, true, postForm), HttpStatus.OK);
	}
	
	//DELETE : Delete Post Based On Id or Email.
	@DeleteMapping("/{postAttr}/{postAttrValue}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("postAttr") String postAttr,
			@PathVariable("postAttrValue") String postAttrValue) {
		PostAttrsEnum attr = PostAttrsEnum.POST_ID;
		if(postAttr.equals("id")) {
			attr = PostAttrsEnum.POST_ID;
		} else if(postAttr.equals("title")) {
			attr = PostAttrsEnum.POST_TITLE;
		} else {
			throw new InvalidFieldException(postAttr, "Post", List.of("id", "title"));
		}
		this.postService.deletePost(postAttrValue, attr);
		//return ResponseEntity.ok(Map.of("message", "Post Deleted Successfully !!"));
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Successfully !!", true) , HttpStatus.OK);
	}
	
	@PutMapping("/{postAttr}/{postAttrValue}")
	public ResponseEntity<ApiResponse> updatePost(@Valid @RequestBody PostForm postForm,
			@PathVariable("postAttr") String postAttr,
			@PathVariable("postAttrValue") String postAttrValue) {
		PostAttrsEnum attr = PostAttrsEnum.POST_ID;
		if(postAttr.equals("id")) {
			attr = PostAttrsEnum.POST_ID;
		} else if(postAttr.equals("title")) {
			attr = PostAttrsEnum.POST_TITLE;
		} else {
			throw new InvalidFieldException(postAttr, "Post", List.of("id", "title"));
		}
		postForm = this.postService.updatePost(postForm, postAttrValue, attr);
		//return ResponseEntity.ok(Map.of("message", "Post Deleted Successfully !!"));
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject("Post Updated Successfully !!", true, postForm) , HttpStatus.OK);
	}
}
