package com.bloggingapi.payload;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class CategoryForm {
	
	private Integer categoryId;
	
	@NotEmpty(message = "Name Of Category Must Not Be Blank Or Null !!")
	private String categoryName;
	
	private String categoryDescription;
	
}
