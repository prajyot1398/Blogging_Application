package com.bloggingapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String resourceName;
	private String fieldName;
	private String fieldValue;
	
	public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
		super(resourceName+" Not Found With "+fieldName+" : "+fieldValue);
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public ResourceNotFoundException(String resourceName) {
		super("No Any Entry For Resource : "+resourceName);
		this.resourceName = resourceName;
	}
}
