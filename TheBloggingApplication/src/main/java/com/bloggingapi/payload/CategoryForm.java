package com.bloggingapi.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class CategoryForm {
	
	@JsonProperty(access = Access.READ_ONLY)
	private Integer categoryId;
	
	@NotEmpty(message = "Name Of Category Must Not Be Blank Or Null !!")
	private String categoryName;
	
	private String categoryDescription;
	
}
