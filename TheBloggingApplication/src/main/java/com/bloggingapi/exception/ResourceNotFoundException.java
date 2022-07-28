package com.bloggingapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String resourceName;
	private String fieldName;
	private String resourceValue;
	
	public ResourceNotFoundException(String resourceName, String fieldName, String resourceValue) {
		super(resourceName+" Not Found With "+fieldName+" : "+resourceValue);
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.resourceValue = resourceValue;
	}
}
