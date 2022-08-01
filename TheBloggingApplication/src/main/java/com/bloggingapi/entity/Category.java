package com.bloggingapi.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blogging_categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "category_name", unique = true, nullable = false, length = 100)
	private String categoryName;
	
	@Column(name = "category_description", length = 500)
	private String categoryDescription;
	
	//mappedBy attr takes name of the field given to Category is Post class
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private Set<Post> postsSet = new HashSet<>();
}
