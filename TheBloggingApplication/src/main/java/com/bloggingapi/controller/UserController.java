package com.bloggingapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bloggingapi.blogenum.PaginationConstatnts;
import com.bloggingapi.blogenum.UserAttrsEnum;
import com.bloggingapi.exception.InvalidFieldException;
import com.bloggingapi.payload.PostForm;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.apiresponse.ApiResponse;
import com.bloggingapi.payload.apiresponse.ApiResponseWithObject;
import com.bloggingapi.payload.multi.PaginationWithContent;
import com.bloggingapi.service.PostService;
import com.bloggingapi.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	//POST :- Create User
	@PreAuthorize("hasRole('ADMIN')")
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
				new ApiResponseWithObject<UserForm>(responseMessage, true, userForm) ,HttpStatus.CREATED);
	}
	
	//Same as above but for signup purpose
	public ResponseEntity<ApiResponse> registerUser(UserForm userForm) {
		
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
				new ApiResponseWithObject<UserForm>(responseMessage, true, userForm) ,HttpStatus.CREATED);
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
	public ResponseEntity<ApiResponse> getUsers(
			@RequestParam(name = "pageNum", defaultValue = PaginationConstatnts.PAGE_NUM , required = false) Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = PaginationConstatnts.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortColumn", defaultValue = PaginationConstatnts.SORT_COLUMN, required = false) String sortColumn,
			@RequestParam(name = "sortAsc", defaultValue = PaginationConstatnts.SORT_ASC, required = false) boolean sortAsc
			) {
			
		PaginationWithContent<List<UserForm>> listUsers = this.userService.getAllUsers(pageNum, pageSize, sortColumn, sortAsc);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject<PaginationWithContent<List<UserForm>>>( "List of users.", true, listUsers), HttpStatus.OK);
	}
	
	//GET :- Get Single User Based On Id or Email
	@GetMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = getUserAttrEnumFromUserAttrString(userAttr);
		UserForm userForm = this.userService.getUser(userAttrValue, attr);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject<UserForm>("User with "+userAttr+" : "+userAttrValue, true, userForm), HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<ApiResponse> searchUserByKeyword(
			@RequestParam(name = "searchName", required = true) String searchName,
			@RequestParam(name = "pageNum", defaultValue = PaginationConstatnts.PAGE_NUM, required = false) Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = PaginationConstatnts.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortColumn", defaultValue = PaginationConstatnts.SORT_COLUMN, required = false) String sortColumn,
			@RequestParam(name = "sortAsc", defaultValue = PaginationConstatnts.SORT_ASC, required = false) boolean sortAsc
			) {
		
		PaginationWithContent<List<UserForm>> pair = this.userService.searchUserByKeyword(searchName,pageNum, pageSize, sortColumn, sortAsc);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject<PaginationWithContent<List<UserForm>>>("List Of Users Containig "+searchName+" In The UserName.", true, pair), HttpStatus.OK);
	}
	
	//PUT :- Update User based on Id or Email
	@PutMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserForm userForm ,
				@PathVariable("userAttr") String userAttr,
				@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = getUserAttrEnumFromUserAttrString(userAttr);
		userForm = this.userService.updateUser(userForm, userAttrValue, attr);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject<UserForm>("User with "+userAttr+" : "+userAttrValue+" is updated.",
						true, userForm), HttpStatus.OK);
	}
	
	//DELETE : Delete User Based On Id or Email.
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userAttr}/{userAttrValue}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue) {
		UserAttrsEnum attr = getUserAttrEnumFromUserAttrString(userAttr);
		this.userService.deleteUser(userAttrValue, attr);
		//return ResponseEntity.ok(Map.of("message", "User Deleted Successfully !!"));
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully !!", true) , HttpStatus.OK);
	}
	
	@GetMapping("/{userAttr}/{userAttrValue}/post")
	public ResponseEntity<ApiResponse> getPostForUser(@PathVariable("userAttr") String userAttr,
			@PathVariable("userAttrValue") String userAttrValue,
			@RequestParam(name = "pageNum", defaultValue = PaginationConstatnts.PAGE_NUM, required = false) Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = PaginationConstatnts.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortColumn", defaultValue = PaginationConstatnts.SORT_COLUMN, required = false) String sortColumn,
			@RequestParam(name = "sortAsc", defaultValue = PaginationConstatnts.SORT_ASC, required = false) boolean sortAsc
			) {
		
		ResponseEntity<ApiResponse> responseEntity = getUser(userAttr, userAttrValue);
		ApiResponse apiResponse = responseEntity.getBody();
		@SuppressWarnings("unchecked")
		UserForm userForm = ((ApiResponseWithObject<UserForm>) apiResponse).getResponseObject();
		PaginationWithContent<List<PostForm>> postFormList = this.postService.getPostsByUser(userForm, 
				userAttr, userAttrValue, pageNum, pageSize, sortColumn, sortAsc);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject<PaginationWithContent<List<PostForm>>>("Posts Of User With "+userAttr+" : "+userAttrValue,
						true, postFormList), HttpStatus.OK);
	}
	
	private UserAttrsEnum getUserAttrEnumFromUserAttrString(String userAttr) {
		UserAttrsEnum attr = UserAttrsEnum.USER_ID;
		if(userAttr.equals("id")) {
			attr = UserAttrsEnum.USER_ID;
		} else if(userAttr.equals("email")) {
			attr = UserAttrsEnum.USER_EMAIL;
		} else {
			throw new InvalidFieldException(userAttr, "User", List.of("id", "email"));
		}
		return attr;
	}
}
