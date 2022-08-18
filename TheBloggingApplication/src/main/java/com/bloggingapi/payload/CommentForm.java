package com.bloggingapi.payload;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

	private Integer commentId;
	
	@NotEmpty(message = "Comment Content Cannot Be Empty !!")
	private String comment;
	
	@NotEmpty(message = "User Email For Comment Cannot Be Empty !!")
	private String userEmail;
	
	@NotEmpty(message = "Post Title For Comment Cannot Be Empty !!")
	private String postTitle;
	
	private Date addedDate;
}
