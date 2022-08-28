package com.bloggingapi.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiParam;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

	@JsonProperty(access = Access.READ_ONLY)
	@ApiParam(hidden = true)
	private Integer commentId;
	
	@NotEmpty(message = "Comment Content Cannot Be Empty !!")
	@ApiParam(hidden = true)
	private String comment;
	
	@NotEmpty(message = "User Email For Comment Cannot Be Empty !!")
	@ApiParam(hidden = true)
	private String userEmail;
	
	@NotEmpty(message = "Post Title For Comment Cannot Be Empty !!")
	@ApiParam(hidden = true)
	private String postTitle;
	
	@JsonProperty(access = Access.READ_ONLY)
	@ApiParam(hidden = true)
	private Date addedDate;
}
