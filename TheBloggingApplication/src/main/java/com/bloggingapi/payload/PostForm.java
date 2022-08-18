package com.bloggingapi.payload;

import java.util.Date;

import javax.validation.constraints.NotEmpty;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostForm {
	
	private Integer postId;
	
	@NotEmpty(message = "Post Title Cannot Be Empty !!")
	private String postTitle;
	
	@NotEmpty(message = "Post Content Cannot Be Empty !!")
	private String postContent;
	
	private String postImage;
	
	private Date postAddedDate; 
	
	@NotEmpty(message = "User Email For Post Cannot Be Empty !!")
	private String userEmail;
	//Don't use entity classes here directly or DTO/Form class if they contain element such as 
	//Post or List<Post>, because it will cause infinite recursion.
	
	@NotEmpty(message = "Category Name For Post Cannot Be Empty !!")
	private String categoryName;
	
	//private MultipartFile postImageFile;
}
