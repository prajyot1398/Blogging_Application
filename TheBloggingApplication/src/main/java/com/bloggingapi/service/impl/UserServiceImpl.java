package com.bloggingapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloggingapi.blogenum.UserAttrsEnum;
import com.bloggingapi.entity.User;
import com.bloggingapi.exception.ElementAlreadyExistException;
import com.bloggingapi.exception.ResourceNotFoundException;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.repository.UserRepo;
import com.bloggingapi.service.UserService;
import com.bloggingapi.util.UserUtil;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo; 
	
	@Override
	public UserForm createUser(UserForm userForm) {
		
		User user = UserUtil.userFormToUser(userForm);
		if(!this.userRepo.existsUserByUserEmail(user.getUserEmail())) {
			user = this.userRepo.save(user);
		}
		else {
			throw new ElementAlreadyExistException("User", "Email", user.getUserEmail());
		}
		return UserUtil.userToUserForm(user);
	}

	@Override
	public UserForm updateUser(UserForm userForm, String userAttrValue, UserAttrsEnum userAttr) {
		User user = getUserFromUserAttr(userAttr, userAttrValue);
		
		UserUtil.updateNullValuesInUserFormFromUser(userForm, user);
		user.setUserId(userForm.getUserId());
		user.setUserName(userForm.getUserName());
		user.setUserEmail(userForm.getUserEmail());
		user.setUserAbout(userForm.getUserAbout());
		user.setUserPassword(userForm.getUserPassword());
		
		user = this.userRepo.save(user);
		return UserUtil.userToUserForm(user);
	}
	
	@Override
	public List<UserForm> getAllUsers() {	
		
		System.out.println(userRepo);				//Object of org.springframework.data.jpa.repository.support.SimpleJpaRepository
		System.out.println(userRepo.getClass().getName());	//com.sun.proxy.$Proxy128 number changes after context reloading
		List<User> userList = this.userRepo.findAll(); 
		List<UserForm> userFormList = UserUtil.getUserFormListFromUserList(userList);
		if(userFormList == null) {
			throw new ResourceNotFoundException("User");
		}
		return userFormList;
	}

	@Override
	public void deleteUser(String userAttrValue, UserAttrsEnum userAttr) {
		User user = getUserFromUserAttr(userAttr, userAttrValue);
		this.userRepo.delete(user);
	}
	
	private User getUserFromUserAttr(UserAttrsEnum userAttr, String userAttrValue) {
		
		User user = null;
		switch(userAttr) {
			case USER_ID:
				user = this.userRepo.findById(Integer.parseInt(userAttrValue))
					.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userAttrValue));
				break;
			case USER_EMAIL:
				user = this.userRepo.findByUserEmail(userAttrValue)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", userAttrValue));
				break;
		}
		return user;
	}

	@Override
	public UserForm getUser(String userAttrValue, UserAttrsEnum userAttr) {
		User user = getUserFromUserAttr(userAttr, userAttrValue);
		return UserUtil.userToUserForm(user);
	}
}
