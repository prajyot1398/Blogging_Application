package com.bloggingapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloggingapi.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{
	
	Optional<Category> findByCategoryName(String categoryName);
	
	boolean existsCategoryByCategoryName(String categoryName);
}
