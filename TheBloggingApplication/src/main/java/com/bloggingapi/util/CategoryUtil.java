package com.bloggingapi.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import com.bloggingapi.entity.Category;
import com.bloggingapi.payload.CategoryForm;

public class CategoryUtil {
	
	public static Category categoryFormToCategory(CategoryForm form) {
		
		Category category = new ModelMapper().map(form, Category.class);
		return category;
	}
	
	public static CategoryForm categoryToCategoryForm(Category category) {
		
		CategoryForm categoryForm = new ModelMapper().map(category, CategoryForm.class);
		return categoryForm;
	}
	
	public static List<Category> getCategoryListFromCategoryFormList(List<CategoryForm> formList) {
		
		List<Category> list = null;
		if(formList != null && formList.size() > 0) {
			list = formList.stream().map(form -> categoryFormToCategory(form)).collect(Collectors.toList());
		}
		return list;
	}
	
	public static List<CategoryForm> getCategoryFormListFromCategoryList(List<Category> categoryList) {
		
		List<CategoryForm> formList = null;
		if(categoryList != null && categoryList.size() > 0) {
			formList = categoryList.stream().map(category -> categoryToCategoryForm(category)).collect(Collectors.toList());
		}
		return formList;
	}
	
	public static void updateNullValuesInCategoryFormFromCategory(CategoryForm form, Category category) {
		
		if(form.getCategoryId() == null) {
			form.setCategoryId(category.getCategoryId());
		}
		if(form.getCategoryName() == null) {
			form.setCategoryName(category.getCategoryName());
		}
		if(form.getCategoryDescription() == null) {
			form.setCategoryDescription(category.getCategoryDescription());
		}
	}
	
	public static void updateNullValuesInCategoryFromCategoryForm(CategoryForm form, Category category) {
		
		if(category.getCategoryId() == null) {
			category.setCategoryId(form.getCategoryId());
		}
		if(category.getCategoryName() == null) {
			category.setCategoryName(form.getCategoryName());
		}
		if(category.getCategoryDescription() == null) {
			category.setCategoryDescription(form.getCategoryDescription());
		}
	}
}
