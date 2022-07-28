package com.bloggingapi.controller;

import java.util.List;
import java.util.Map;

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
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//POST :- Create User
	@PostMapping("/")
	public ResponseEntity<UserForm> createUser(@RequestBody UserForm userForm) {
		
		userForm = this.userService.createUser(userForm);
		return new ResponseEntity<UserForm>(userForm, HttpStatus.CREATED);
	}
	
	//GET :- Get All Users
	@GetMapping("/")
	public ResponseEntity<List<UserForm>> getUsers() {
		
		List<UserForm> listUsers = this.userService.getAllUsers();
		return new ResponseEntity<List<UserForm>>(listUsers, HttpStatus.OK);
	}
	
	//GET :- Get Single User Based On Id or Email
	@GetMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<UserForm> getUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		}
		UserForm userForm = this.userService.getUser(userAttrValue, attr);
		return new ResponseEntity<UserForm>(userForm, HttpStatus.OK);
	}
	//PUT :- Update User based on Id or Email
	@PutMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<UserForm> updateUser(@RequestBody UserForm userForm ,
				@PathVariable("userAttr") String userAttr,
				@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		}
		userForm = this.userService.updateUser(userForm, userAttrValue, attr);
		return ResponseEntity.ok(userForm);
	}
	
	//DELETE : Delete User Based On Id or Email.
	@DeleteMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<?> deleteUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		}
		this.userService.deleteUser(userAttrValue, attr);
		return ResponseEntity.ok(Map.of("message", "User Deleted Successfully !!"));
	}
}
