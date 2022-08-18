package com.bloggingapi.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bloggingapi.blogenum.PostAttrsEnum;
import com.bloggingapi.entity.Category;
import com.bloggingapi.entity.Comment;
import com.bloggingapi.entity.Post;
import com.bloggingapi.entity.User;
import com.bloggingapi.exception.ElementAlreadyExistException;
import com.bloggingapi.exception.InvalidFileFormatException;
import com.bloggingapi.exception.InvalidParentEntityException;
import com.bloggingapi.exception.ResourceNotFoundException;
import com.bloggingapi.payload.CategoryForm;
import com.bloggingapi.payload.CommentForm;
import com.bloggingapi.payload.PostForm;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.multi.PaginationForm;
import com.bloggingapi.payload.multi.PaginationWithContent;
import com.bloggingapi.repository.CategoryRepo;
import com.bloggingapi.repository.CommentRepo;
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
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Value("${project.postImagesParentDir}")
	private String POST_IMAGES_PARENT_DIR;
	
	@Value("${project.postImagesDir}")
	private String POST_IMAGES_DIR;
	
	private final String DEFAULT_IMAGE = "default.jpg";
	
	@Override
	public PostForm createPost(PostForm postForm, MultipartFile postImageFile) {
		
		User user = userRepo.findByUserEmail(postForm.getUserEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserName/Email", postForm.getUserEmail()));
		Category category = categoryRepo.findByCategoryName(postForm.getCategoryName())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Name", postForm.getCategoryName()));
		
		Post post = PostUtil.postFormToPost(postForm);
		PostUtil.updateNullValuesInPostFromPostForm(postForm, post);
		post.setCategory(category);
		post.setUser(user);
		
		if(!postRepo.existsPostByPostTitle(post.getPostTitle())) {
			//Logic to save post image in filesystem and update postImage.
			String postImageNewName = this.getPostImageNameFromFile(postImageFile);
			try {
				this.savePostImageAndGenerateURI(postImageNewName, postImageFile);
			}catch(Exception exp) {
				exp.printStackTrace();	
				postImageNewName = this.DEFAULT_IMAGE;
			}finally {
				String addedFileURI = ServletUriComponentsBuilder.fromCurrentContextPath().path(this.POST_IMAGES_DIR + "/").path(postImageNewName).toUriString();
				post.setPostImage(addedFileURI);
				post = this.postRepo.save(post);
			}
		} else {
			throw new ElementAlreadyExistException("Post", "Post Title", post.getPostTitle());
		}
		
		return PostUtil.postToPostForm(post);
	}

	@Override
	public PostForm updatePost(PostForm postForm, String postAttrValue, PostAttrsEnum postAttr,  MultipartFile postImageFile) {
		
		Post post = getPostFromPostAttr(postAttr, postAttrValue);
		if(!post.getUser().getUserEmail().equals(postForm.getUserEmail())) {
			throw new InvalidParentEntityException("User", "Username/Email", postForm.getUserEmail());
		}
		if(!post.getCategory().getCategoryName().equals(postForm.getCategoryName())) {
			throw new InvalidParentEntityException("Category", "Categoryname", postForm.getUserEmail());
		}
		
		PostUtil.updateNullValuesInPostFromPostForm(postForm, post);
		
		if(postImageFile != null) {
			
			String postImageNewName = this.getPostImageNameFromFile(postImageFile);
			try {
				this.savePostImageAndGenerateURI(postImageNewName, postImageFile);
			}catch(Exception exp) {
				exp.printStackTrace();	
				postImageNewName = this.DEFAULT_IMAGE;
			}finally {
				String addedFileURI = ServletUriComponentsBuilder.fromCurrentContextPath().path(this.POST_IMAGES_DIR + "/").path(postImageNewName).toUriString();
				post.setPostImage(addedFileURI);
			}
		}
		
		post.setPostTitle(postForm.getPostTitle());
		post.setPostContent(postForm.getPostContent());
		
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
		Page<Post> pageForm = this.postRepo.findByPostTitleContaining(keyWord, pagination);
		List<Post> postList = pageForm.getContent();
		if(postList == null || postList.isEmpty())
			throw new ResourceNotFoundException("Posts", "Title Containing", keyWord);
		List<PostForm> postFormList = PostUtil.getPostFormListFromPostList(postList);
		PaginationForm paginationForm = PaginationForm.getPaginationFormFromPage(pageForm, sortColumn, sortAsc);
		return PaginationWithContent.of(postFormList, paginationForm);
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
	
	private String getPostImageNameFromFile(MultipartFile file) {
		
		String imageName = null;
		if(file != null) {
			String originalFileName = file.getOriginalFilename();
			String randomId = UUID.randomUUID().toString();
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg")) {
				imageName = randomId.concat(extension);
			}
			else {
				throw new InvalidFileFormatException(extension, "Post Image", List.of("jpeg", "jpg", "png"));
			}
		}else {
			imageName = this.DEFAULT_IMAGE;
		}
		return imageName;
	}
	
	private void savePostImageAndGenerateURI(String imageName, MultipartFile file) throws IOException{
		
		if(file != null) { 
			if(checkAndCreateFolderIfNotExisted()) {
				Files.copy(file.getInputStream(), Paths.get(this.POST_IMAGES_PARENT_DIR + this.POST_IMAGES_DIR + File.separator + imageName), StandardCopyOption.REPLACE_EXISTING);
			}else {
				throw new RuntimeException("Cannot Create Parent Folder For PostImages.");
			}
		}	
	}
	
	private boolean checkAndCreateFolderIfNotExisted() throws IOException{
		
		File parentFolder = new File(this.POST_IMAGES_PARENT_DIR + this.POST_IMAGES_DIR);
		if(!parentFolder.exists()) {
			return parentFolder.mkdir();
		}
		return true;
	}

	@Override
	public PostForm addOrDeleteComment(CommentForm commentForm, boolean delete) {
		
		User user = userRepo.findByUserEmail(commentForm.getUserEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserName/Email", commentForm.getUserEmail()));
		
		Post post = this.postRepo.findByPostTitle(commentForm.getPostTitle())
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Title", commentForm.getPostTitle()));
		
		Comment comment = PostUtil.commentFormToComment(commentForm);
		PostUtil.updateNullValuesInCommentFromCommentForm(commentForm, comment);
		comment.setPost(post);
		comment.setUser(user);
		
		if(delete) {
			if(!this.commentRepo.existsById(comment.getCommentId())) {
				throw new ResourceNotFoundException("Comment", "CommentId", String.valueOf(comment.getCommentId()));
			}
			this.commentRepo.delete(comment);
		}
		else {
			comment = this.commentRepo.save(comment);
		}
		post.setComments(this.commentRepo.findByPost(post));
		
		return PostUtil.postToPostForm(post);
	}
}
