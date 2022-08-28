package com.bloggingapi.payload;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostForm {
	
	@JsonProperty(access = Access.READ_ONLY)
	@ApiParam(hidden = true, allowEmptyValue = true)
	private Integer postId;
	
	@NotEmpty(message = "Post Title Cannot Be Empty !!")
	@ApiParam(required = true)
	private String postTitle;
	
	@NotEmpty(message = "Post Content Cannot Be Empty !!")
	@ApiParam(required = true)
	private String postContent;
	
	@JsonProperty(access = Access.READ_ONLY)
	@ApiParam(hidden = true)
	private String postImage;
	
	@JsonProperty(access = Access.READ_ONLY)
	@ApiParam(hidden = true)
	private Date postAddedDate; 
	
	@NotEmpty(message = "User Email For Post Cannot Be Empty !!")
	@ApiParam(required = true)
	private String userEmail;
	//Don't use entity classes here directly or DTO/Form class if they contain element such as 
	//Post or List<Post>, because it will cause infinite recursion.
	
	@NotEmpty(message = "Category Name For Post Cannot Be Empty !!")
	@ApiParam(required = true)
	private String categoryName;
	
	// private MultipartFile postImageFile;
	@JsonProperty(access = Access.READ_ONLY)
	private Set<CommentForm> comments = new HashSet<>();
}
