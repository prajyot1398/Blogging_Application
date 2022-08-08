package com.bloggingapi.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;

import com.bloggingapi.entity.User;
import com.bloggingapi.payload.UserForm;

public class UserUtil {
	
	//These two below methods can be written using Model-Mapper libraries.
	
	public static User userFormToUser(UserForm userForm) {
		
		//While using ModelMapper use variable names of both the classes same.
		User user = new ModelMapper().map(userForm, User.class);
		return user;
		/*if(userForm != null) {
			User user = new User();
			user.setUserId(userForm.getUserId());
			user.setUserName(userForm.getUserName());
			user.setUserEmail(userForm.getUserEmail());
			user.setUserAbout(userForm.getUserAbout());
			user.setUserPassword(userForm.getUserPassword());
			return user; 
		}
		else {
			return null;
		}*/
	}
	
	public static UserForm userToUserForm(User user) {
		
		UserForm userForm = new ModelMapper().map(user, UserForm.class);
		updateNullValuesInUserFormFromUser(userForm, user);
		return userForm;
		/*if(user != null) {
			UserForm form = new UserForm();
			form.setUserId(user.getUserId());
			form.setUserName(user.getUserName());
			form.setUserEmail(user.getUserEmail());
			form.setUserPassword(user.getUserPassword());
			form.setUserAbout(user.getUserAbout());
			return form;
		}else {
			return null;
		}*/
	}
	
	public static List<User> getUserListFromUserFormList(List<UserForm> formList) {
		
		//List<User> userList = new ArrayList<User>(); //If used normal array to array copy
		List<User> userList = null;		//If used Stream API
		if(formList != null && formList.size() > 0) {
			//Method 1
			/*
			 * for(UserForm form : formList) { userList.add(userFormToUser(form)); }
			 */
			//Method 2
			userList = formList.stream().map(form -> userFormToUser(form)).collect(Collectors.toList());
			
		}
		return userList;
	}
	
	public static List<UserForm> getUserFormListFromUserList(List<User> userList) {
		
		//List<UserForm> formList = new ArrayList<UserForm>();	//If used normal copying
		List<UserForm> formList = null;		//If used Stream API
		
		if(userList != null && userList.size() > 0) {
			//Method 1
			/*
			 * for(User user : userList) { formList.add(userToUserForm(user)); }
			 */
			//Method 2
			formList = userList.stream().map(user -> userToUserForm(user)).collect(Collectors.toList());
		}
		return formList;
	}
	
	public static void updateNullValuesInUserFormFromUser(UserForm form, User user) {
		
		if(form.getUserId() == null) {
			form.setUserId(user.getUserId());
		}
		if(form.getUserEmail() == null) {
			form.setUserEmail(user.getUserEmail());
		}
		if(form.getUserName() == null) {
			form.setUserName(user.getUserName());
		}
		if(form.getUserAbout() == null) {
			form.setUserAbout(user.getUserAbout());
		}
		if(form.getUserPassword() == null) {
			form.setUserPassword(user.getUserPassword());
		}
		if(form.getUserAddedDate() == null) {
			form.setUserAddedDate(user.getAddedDate());
		}
	}
	
	public static void updateNullValuesInUserFromUserForm(UserForm form, User user) {
		
		if(user.getUserId() == null) {
			user.setUserId(form.getUserId());
		}
		if(user.getUserEmail() == null) {
			user.setUserEmail(form.getUserEmail());
		}
		if(user.getUserName() == null) {
			user.setUserName(form.getUserName());
		}
		if(user.getUserAbout() == null) {
			user.setUserAbout(form.getUserAbout());
		}
		if(user.getUserPassword() == null) {
			user.setUserPassword(form.getUserPassword());
		}
		if(user.getAddedDate() == null) {
			if(form.getUserAddedDate() != null) {
				user.setAddedDate(form.getUserAddedDate());
			}
			else {
				user.setAddedDate(new Date());
			}
		}
	}
}
