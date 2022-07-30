package com.bloggingapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloggingapi.blogenum.CategoryAttrsEnum;
import com.bloggingapi.entity.Category;
import com.bloggingapi.exception.ElementAlreadyExistException;
import com.bloggingapi.exception.ResourceNotFoundException;
import com.bloggingapi.payload.CategoryForm;
import com.bloggingapi.repository.CategoryRepo;
import com.bloggingapi.service.CategoryService;
import com.bloggingapi.util.CategoryUtil;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public CategoryForm createCategory(CategoryForm categoryForm) {
		
		Category category = CategoryUtil.categoryFormToCategory(categoryForm);
		
		if(!this.categoryRepo.existsCategoryByCategoryName(category.getCategoryName())) {
			category = this.categoryRepo.save(category);
		}
		else {
			throw new ElementAlreadyExistException("Category", "Name", category.getCategoryName());
		}
		
		return CategoryUtil.categoryToCategoryForm(category);
	}

	@Override
	public List<CategoryForm> getAllCategories() {
		
		List<Category> categoryList = this.categoryRepo.findAll();
		List<CategoryForm> formList = CategoryUtil.getCategoryFormListFromCategoryList(categoryList);
		if(formList == null) {
			throw new ResourceNotFoundException("Category");
		}
		return formList;
	}

	@Override
	public CategoryForm getCategory(String categoryAttrValue, CategoryAttrsEnum categoryAttr) {
		
		Category category = getCategoryFromCategoryAttr(categoryAttr, categoryAttrValue);
		return CategoryUtil.categoryToCategoryForm(category);
	}

	@Override
	public CategoryForm updateCategory(CategoryForm form, String categoryAttrValue, CategoryAttrsEnum categoryAttr) {
		
		Category category = getCategoryFromCategoryAttr(categoryAttr, categoryAttrValue);
		CategoryUtil.updateNullValuesInCategoryFormFromCategory(form, category);
		
		category.setCategoryId(form.getCategoryId());
		category.setCategoryName(form.getCategoryName());
		category.setCategoryDescription(form.getCategoryDescription());
		this.categoryRepo.save(category);
		
		return CategoryUtil.categoryToCategoryForm(category);
	}

	@Override
	public void deleteCategory(String categoryAttrValue, CategoryAttrsEnum categoryAttr) {
		
		Category category = getCategoryFromCategoryAttr(categoryAttr, categoryAttrValue);
		this.categoryRepo.delete(category);
	}
	
	private Category getCategoryFromCategoryAttr(CategoryAttrsEnum categoryAttr, String categoryAttrValue) {
		
		Category category = null;
		switch(categoryAttr) {
			case CATEGORY_ID:
				category = this.categoryRepo.findById(Integer.parseInt(categoryAttrValue))
					.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryAttrValue));
				break;
			case CATEGORY_NAME:
				category = this.categoryRepo.findByCategoryName(categoryAttrValue)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Name", categoryAttrValue));
				break;
		}
		return category;
	}

}
