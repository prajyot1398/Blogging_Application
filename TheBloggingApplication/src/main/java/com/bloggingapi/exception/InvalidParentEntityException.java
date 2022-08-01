package com.bloggingapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidParentEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String resourceName;
	private String fieldName;
	private String fieldValue;
	
	public InvalidParentEntityException(String resourceName, String fieldName, String fieldValue) {
		super("Invalid Parent Entity : "+resourceName+" With "+fieldName+" : "+fieldValue);
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
}
