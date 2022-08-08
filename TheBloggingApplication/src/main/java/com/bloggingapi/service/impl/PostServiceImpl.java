package com.bloggingapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bloggingapi.blogenum.PostAttrsEnum;
import com.bloggingapi.entity.Category;
import com.bloggingapi.entity.Post;
import com.bloggingapi.entity.User;
import com.bloggingapi.exception.ElementAlreadyExistException;
import com.bloggingapi.exception.InvalidParentEntityException;
import com.bloggingapi.exception.ResourceNotFoundException;
import com.bloggingapi.payload.CategoryForm;
import com.bloggingapi.payload.PostForm;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.multi.PaginationForm;
import com.bloggingapi.payload.multi.PaginationWithContent;
import com.bloggingapi.repository.CategoryRepo;
import com.bloggingapi.repository.PostRepo;
import com.bloggingapi.repository.UserRepo;
import com.bloggingapi.service.PostService;
import com.bloggingapi.util.CategoryUtil;
import com.bloggingapi.util.PostUtil;
import com.bloggingapi.util.UserUtil;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public PostForm createPost(PostForm postForm) {
		
		User user = userRepo.findByUserEmail(postForm.getUserEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserName/Email", postForm.getUserEmail()));
		Category category = categoryRepo.findByCategoryName(postForm.getCategoryName())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Name", postForm.getCategoryName()));
		
		Post post = PostUtil.postFormToPost(postForm);
		PostUtil.updateNullValuesInPostFromPostForm(postForm, post);
		post.setCategory(category);
		post.setUser(user);
		
		if(!postRepo.existsPostByPostTitle(post.getPostTitle())) {
			post = this.postRepo.save(post);
		} else {
			throw new ElementAlreadyExistException("Post", "Post Title", post.getPostTitle());
		}
		
		return PostUtil.postToPostForm(post);
	}

	@Override
	public PostForm updatePost(PostForm postForm, String postAttrValue, PostAttrsEnum postAttr) {
		
		Post post = getPostFromPostAttr(postAttr, postAttrValue);
		if(!post.getUser().getUserEmail().equals(postForm.getUserEmail())) {
			throw new InvalidParentEntityException("User", "Username/Email", postForm.getUserEmail());
		}
		if(!post.getCategory().getCategoryName().equals(postForm.getCategoryName())) {
			throw new InvalidParentEntityException("Category", "Categoryname", postForm.getUserEmail());
		}
		
		PostUtil.updateNullValuesInPostFromPostForm(postForm, post);
		
		post.setPostTitle(postForm.getPostTitle());
		post.setPostContent(postForm.getPostContent());
		post.setPostImage(postForm.getPostImage());
		
		post = this.postRepo.save(post);
		return PostUtil.postToPostForm(post);
	}

	@Override
	public PaginationWithContent<List<PostForm>> getAllPosts(final Integer pageNum, 
			final Integer pageSize, final String sortColumn, final boolean sortAsc) {
		
		Pageable pagination = PaginationForm.getPagination(pageNum, pageSize, sortColumn, sortAsc);
		
		Page<Post> pageForm = this.postRepo.findAll(pagination);
		List<Post> posts = pageForm.getContent();
		List<PostForm> postFormList = PostUtil.getPostFormListFromPostList(posts);
		if(postFormList == null) {
			throw new ResourceNotFoundException("Posts");
		}
		
		PaginationForm paginationForm = PaginationForm.getPaginationFormFromPage(pageForm, sortColumn, sortAsc);
		return PaginationWithContent.of(postFormList, paginationForm);
	}

	@Override
	public void deletePost(String postAttrValue, PostAttrsEnum postAttr) {
		
		Post post = getPostFromPostAttr(postAttr, postAttrValue);
		this.postRepo.delete(post);
	}

	@Override
	public PostForm getPostByAttr(String postAttrValue, PostAttrsEnum postAttr) {
		
		Post post = getPostFromPostAttr(postAttr, postAttrValue);
		return PostUtil.postToPostForm(post);
	}

	@Override
	public PaginationWithContent<List<PostForm>> getPostsByUser(UserForm userForm, 
			String userAttr, String userAttrValue, 
			final Integer pageNum, final Integer pageSize, 
			final String sortColumn, final boolean sortAsc) {
		
		User user = UserUtil.userFormToUser(userForm);
		Pageable pagination = PaginationForm.getPagination(pageNum, pageSize, sortColumn, sortAsc);
		Page<Post> pageForm = this.postRepo.findByUser(user, pagination);
		List<Post> postList = pageForm.getContent();
		if(postList == null || postList.isEmpty())
			throw new ResourceNotFoundException("Posts",  "User "+userAttr, userAttrValue);
		
		List<PostForm> postFormList = PostUtil.getPostFormListFromPostList(postList);
		PaginationForm paginationForm = PaginationForm.getPaginationFormFromPage(pageForm, sortColumn, sortAsc);
		return PaginationWithContent.of(postFormList, paginationForm);
	}

	@Override
	public PaginationWithContent<List<PostForm>> getPostsByCategory(CategoryForm categoryForm, 
			String categoryAttr, String categoryAttrValue, 
			final Integer pageNum, final Integer pageSize, 
			final String sortColumn, final boolean sortAsc) {
		
		Category category = CategoryUtil.categoryFormToCategory(categoryForm);
		Pageable pagination = PaginationForm.getPagination(pageNum, pageSize, sortColumn, sortAsc);
		Page<Post> pageForm = this.postRepo.findByCategory(category, pagination);
		List<Post> postList = pageForm.getContent();
		if(postList == null || postList.isEmpty())
			throw new ResourceNotFoundException("Posts", "Category "+categoryAttr, categoryAttrValue);
				
		List<PostForm> postFormList = PostUtil.getPostFormListFromPostList(postList);
		PaginationForm paginationForm = PaginationForm.getPaginationFormFromPage(pageForm, sortColumn, sortAsc);
		return PaginationWithContent.of(postFormList, paginationForm);
	}

	@Override
	public PaginationWithContent<List<PostForm>> searchPostByKeyword(String keyWord, 
			final Integer pageNum, final Integer pageSize, 
			final String sortColumn, final boolean sortAsc) {
		
		Pageable pagination = PaginationForm.getPagination(pageNum, pageSize, sortColumn, sortAsc);
		
		return null;
	}
	
	private Post getPostFromPostAttr(PostAttrsEnum postAttr, String postAttrValue) {
		
		Post post = null;
		switch(postAttr) {
			
			case POST_ID:
				post = this.postRepo.findById(Integer.parseInt(postAttrValue))
						.orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postAttrValue));
				break;
			case POST_TITLE:
				post = this.postRepo.findByPostTitle(postAttrValue)
					.orElseThrow(() -> new ResourceNotFoundException("Post", "Title", postAttrValue));
				break;
		}
		
		return post;
	}
}
