package com.bloggingapi.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//This will be used as DTO i.e. Data Transfer Object.
//It can be used to avoid direct usage of entity class as entity class is using persistance API.

@NoArgsConstructor
@Getter
@Setter
public class UserForm {
	
	private String userId;
	private String userName;
	private String userEmail;
	private String userAbout;
	private String userPassword;
	
}
