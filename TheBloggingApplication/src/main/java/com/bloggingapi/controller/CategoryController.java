package com.bloggingapi.controller;

import javax.validation.Valid;

import java.util.List;

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

import com.bloggingapi.blogenum.CategoryAttrsEnum;
import com.bloggingapi.exception.InvalidFieldException;
import com.bloggingapi.payload.CategoryForm;
import com.bloggingapi.payload.apiresponse.ApiResponse;
import com.bloggingapi.payload.apiresponse.ApiResponseWithObject;
import com.bloggingapi.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	//POST : Create Category
	@PostMapping("/")
	public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CategoryForm categoryForm) {
		
		String responseMessage = null;
		if(categoryForm.getCategoryId() != null) {
			responseMessage = "You have provided categoryId in POST Request, it might "
					+ "override existing category if present with same id. It won't consider"
					+ " categoryId if category not already exists as ids are auto-incremented";
		}else {
			responseMessage = "Category Created Successfully !!"; 
		}
		categoryForm = this.categoryService.createCategory(categoryForm);
		return new ResponseEntity<ApiResponse>(new ApiResponseWithObject(responseMessage, true, categoryForm), HttpStatus.CREATED);
	}
	
	//GET : Get all the categories
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getAllCategories() {
		
		List<CategoryForm> list = this.categoryService.getAllCategories();
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject("List Of Categories", true, list), HttpStatus.OK);
	}
	
	//GET : Get single category based on Id or Name
	@GetMapping("{categoryAttr}/{categoryAttrValue}")
	public ResponseEntity<ApiResponse> getCategory(@PathVariable("categoryAttr") String categoryAttr, 
				@PathVariable("categoryAttrValue") String categoryAttrValue) {
		
		CategoryAttrsEnum attr = null;
		if(categoryAttr.equals("id")) {
			attr = CategoryAttrsEnum.CATEGORY_ID;
		} else if(categoryAttr.equals("name")) {
			attr = CategoryAttrsEnum.CATEGORY_NAME;
		} else {
			throw new InvalidFieldException(categoryAttr, "Category", List.of("id", "name"));
		}
		
		CategoryForm form = this.categoryService.getCategory(categoryAttrValue, attr);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject("Category With : "+categoryAttr+" And Value : "+categoryAttrValue, true, form), HttpStatus.OK
				);
	}
	
	//PUT : Update single category based on name or id.
	@PutMapping("{categoryAttr}/{categoryAttrValue}")
	public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryAttr") String categoryAttr, 
			@PathVariable("categoryAttrValue") String categoryAttrValue, @Valid @RequestBody CategoryForm form) {
		
		CategoryAttrsEnum attr = null;
		if(categoryAttr.equals("id")) {
			attr = CategoryAttrsEnum.CATEGORY_ID;
		} else if(categoryAttr.equals("name")) {
			attr = CategoryAttrsEnum.CATEGORY_NAME;
		} else {
			throw new InvalidFieldException(categoryAttr, "Category", List.of("id", "name"));
		}
		
		form = this.categoryService.updateCategory(form, categoryAttrValue, attr);
		return new ResponseEntity<ApiResponse>(
				new ApiResponseWithObject("Category With : "+categoryAttr+" And Value : "+categoryAttrValue+" is updated.", true, form), HttpStatus.OK
				);
	}
	
	//DELETE : Delete Single category based on id or name
	@DeleteMapping("{categoryAttr}/{categoryAttrValue}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryAttr") String categoryAttr, 
			@PathVariable("categoryAttrValue") String categoryAttrValue) {
		
		CategoryAttrsEnum attr = null;
		if(categoryAttr.equals("id")) {
			attr = CategoryAttrsEnum.CATEGORY_ID;
		} else if(categoryAttr.equals("name")) {
			attr = CategoryAttrsEnum.CATEGORY_NAME;
		} else {
			throw new InvalidFieldException(categoryAttr, "Category", List.of("id", "name"));
		}
		
		this.categoryService.deleteCategory(categoryAttrValue, attr);
		
		return new ResponseEntity<ApiResponse>(
				new ApiResponse("Category Delete Successfully !!", true), HttpStatus.OK
				);
	}
}
