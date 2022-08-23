package com.bloggingapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bloggingapi.blogenum.UserAttrsEnum;
import com.bloggingapi.entity.User;
import com.bloggingapi.exception.ElementAlreadyExistException;
import com.bloggingapi.exception.ResourceNotFoundException;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.multi.PaginationForm;
import com.bloggingapi.payload.multi.PaginationWithContent;
import com.bloggingapi.repository.RoleRepo;
import com.bloggingapi.repository.UserRepo;
import com.bloggingapi.service.UserService;
import com.bloggingapi.util.UserUtil;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserForm createUser(UserForm userForm) {
		
		User user = UserUtil.userFormToUser(userForm);
		UserUtil.updateNullValuesInUserFromUserForm(userForm, user);
		if(!this.userRepo.existsUserByUserEmail(user.getUserEmail())) {
			user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
			user.getRoles().add(this.roleRepo.findByRoleName("ROLE_NORMAL"));
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
		
		boolean isPasswordUpdated = false;
		if(userForm.getUserPassword() != null && !user.getUserPassword().equals(userForm.getUserPassword()))
			isPasswordUpdated = true;
	
		UserUtil.updateNullValuesInUserFormFromUser(userForm, user);
		user.setUserId(userForm.getUserId());
		user.setUserName(userForm.getUserName());
		user.setUserEmail(userForm.getUserEmail());
		user.setUserAbout(userForm.getUserAbout());
		if(isPasswordUpdated)
			user.setUserPassword(this.passwordEncoder.encode(userForm.getUserPassword()));
		
		user = this.userRepo.save(user);
		return UserUtil.userToUserForm(user);
	}
	
	@Override
	public PaginationWithContent<List<UserForm>> getAllUsers(
			Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc) {	
		
		System.out.println(userRepo);				//Object of org.springframework.data.jpa.repository.support.SimpleJpaRepository
		System.out.println(userRepo.getClass().getName());	//com.sun.proxy.$Proxy128 number changes after context reloading
		
		Pageable pagination = PaginationForm.getPagination(pageNum, pageSize, sortColumn, sortAsc);
		Page<User> pageForm = this.userRepo.findAll(pagination); 
		List<User> userList = pageForm.getContent();
		List<UserForm> userFormList = UserUtil.getUserFormListFromUserList(userList);
		if(userFormList == null) {
			throw new ResourceNotFoundException("User");
		}
		
		PaginationForm paginationForm = PaginationForm.getPaginationFormFromPage(pageForm, sortColumn, sortAsc);
		return PaginationWithContent.of(userFormList, paginationForm);
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

	@Override
	public PaginationWithContent<List<UserForm>> searchUserByKeyword(String keyword, 
			Integer pageNum, Integer pageSize,
			String sortColumn, boolean sortAsc) {
		
		Pageable pagination = PaginationForm.getPagination(pageNum, pageSize, sortColumn, sortAsc);
		Page<User> pageForm = this.userRepo.findByUserNameContaining(keyword, pagination); 
		List<User> userList = pageForm.getContent();
		List<UserForm> userFormList = UserUtil.getUserFormListFromUserList(userList);
		if(userFormList == null) {
			throw new ResourceNotFoundException("User", "Username Containing", keyword);
		}
		
		PaginationForm paginationForm = PaginationForm.getPaginationFormFromPage(pageForm, sortColumn, sortAsc);
		return PaginationWithContent.of(userFormList, paginationForm);
	}
}
