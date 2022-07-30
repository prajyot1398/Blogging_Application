package com.bloggingapi.service;

import java.util.List;

import com.bloggingapi.blogenum.CategoryAttrsEnum;
import com.bloggingapi.payload.CategoryForm;

public interface CategoryService {

		CategoryForm createCategory(CategoryForm categoryForm);
		
		List<CategoryForm> getAllCategories();
		
		CategoryForm getCategory(String categoryAttrValue, CategoryAttrsEnum categoryAttr);
		
		CategoryForm updateCategory(CategoryForm form, String categoryAttrValue, CategoryAttrsEnum categoryAttr);
		
		void deleteCategory(String categoryAttrValue, CategoryAttrsEnum categoryAttr);
}
