package com.bloggingapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blogging_categories")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, length = 10)
	private final String categoryId;
	
	@Column(name= "category_name", nullable = false, length = 100)
	private final String categoryName;
	
	@Column(name = "category_description", length = 500)
	private String categoryyDescription;
}
