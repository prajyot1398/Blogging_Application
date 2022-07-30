package com.bloggingapi.service;

import java.util.List;

import com.bloggingapi.blogenum.UserAttrsEnum;
import com.bloggingapi.payload.UserForm;

public interface UserService {
	
	UserForm createUser(UserForm userForm);
	
	UserForm updateUser(UserForm userForm, String userAttrValue, UserAttrsEnum userAttr);
	
	List<UserForm> getAllUsers();
	
	void deleteUser(String userAttrValue, UserAttrsEnum userAttr);

	UserForm getUser(String userAttrValue, UserAttrsEnum userAttr);
	
}
