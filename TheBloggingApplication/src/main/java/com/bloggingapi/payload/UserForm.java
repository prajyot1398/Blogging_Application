package com.bloggingapi.payload;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//This will be used as DTO i.e. Data Transfer Object.
//It can be used to avoid direct usage of entity class as entity class is using persistance API.

@NoArgsConstructor
@Getter
@Setter
public class UserForm {
	
	@JsonProperty(access = Access.READ_ONLY)
	private Integer userId;
	
	@NotEmpty(message = "Name Of User Must Not Be Blank Or Null !!")
	private String userName;
	@Email(message = "Email Must Be In Correct Format !!")
	@NotEmpty(message = "User Email Must Not Be Blank Or Null !!")
	private String userEmail;
	private String userAbout;
	@NotEmpty(message = "Password Must No Be Blank Or Null !!")
	@Size(min = 3, max = 10, message = "Passoword Size Must Be In 3 to 10 !!")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String userPassword;
	@JsonProperty(access = Access.READ_ONLY)
	private Date userAddedDate;
}
