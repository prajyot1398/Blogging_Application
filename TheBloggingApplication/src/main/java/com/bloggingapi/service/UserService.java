package com.bloggingapi.service;

import java.util.List;

import com.bloggingapi.blogenum.UserAttrsEnum;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.multi.PaginationWithContent;

public interface UserService {
	
	UserForm createUser(UserForm userForm);
	
	UserForm updateUser(UserForm userForm, String userAttrValue, UserAttrsEnum userAttr);
	
	PaginationWithContent<List<UserForm>> getAllUsers(Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc);
	
	PaginationWithContent<List<UserForm>> searchUserByKeyword(String keyword,
			Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc);
	
	void deleteUser(String userAttrValue, UserAttrsEnum userAttr);

	UserForm getUser(String userAttrValue, UserAttrsEnum userAttr);
	
}
