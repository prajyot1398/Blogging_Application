package com.bloggingapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bloggingapi.blogenum.UserAttrsEnum;
import com.bloggingapi.exception.InvalidFieldException;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.apiresponse.ApiResponse;
import com.bloggingapi.payload.apiresponse.ApiResponseWithObject;
import com.bloggingapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//POST :- Create User
	@PostMapping("/")
	public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserForm userForm) {
		
		String responseMessage = null;
		if(userForm.getUserId() != null) {
			responseMessage = "You have provided userId in POST Request, it might "
					+ "override existing user if present with same id. It won't consider"
					+ " userId if user not already exists as ids are auto-incremented";
		}else {
			responseMessage = "User created successfully !!"; 
		}
		userForm = this.userService.createUser(userForm);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject(responseMessage, true, userForm) ,HttpStatus.CREATED);
	}
	
	/*
	//GET :- Get All Users
	@GetMapping("/")
	public ResponseEntity<List<UserForm>> getUsers() {
		
		List<UserForm> listUsers = this.userService.getAllUsers();
		return new ResponseEntity<List<UserForm>>(listUsers, HttpStatus.OK);
	}*/
	//GET :- Get All Users
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getUsers() {
			
		List<UserForm> listUsers = this.userService.getAllUsers();
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject( "List of users.", true, listUsers), HttpStatus.OK);
	}
	
	//GET :- Get Single User Based On Id or Email
	@GetMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("id")) {
			attr = UserAttrsEnum.USER_ID;
		} else if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		} else {
			throw new InvalidFieldException(userAttr, "User", List.of("id", "email"));
		}
		UserForm userForm = this.userService.getUser(userAttrValue, attr);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject("User with "+userAttr+" : "+userAttrValue, true, userForm), HttpStatus.OK);
	}
	//PUT :- Update User based on Id or Email
	@PutMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserForm userForm ,
				@PathVariable("userAttr") String userAttr,
				@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("id")) {
			attr = UserAttrsEnum.USER_ID;
		} else if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		} else {
			throw new InvalidFieldException(userAttr, "User", List.of("id", "email"));
		}
		userForm = this.userService.updateUser(userForm, userAttrValue, attr);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject("User with "+userAttr+" : "+userAttrValue+" is updated.",
						true, userForm), HttpStatus.OK);
	}
	
	//DELETE : Delete User Based On Id or Email.
	@DeleteMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("id")) {
			attr = UserAttrsEnum.USER_ID;
		} else if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		} else {
			throw new InvalidFieldException(userAttr, "User", List.of("id", "email"));
		}
		this.userService.deleteUser(userAttrValue, attr);
		//return ResponseEntity.ok(Map.of("message", "User Deleted Successfully !!"));
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully !!", true) , HttpStatus.OK);
	}
}
