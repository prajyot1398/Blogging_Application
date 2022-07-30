package com.bloggingapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementAlreadyExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String resourceName;
	private String fieldName;
	private String fieldValue;
	
	public ElementAlreadyExistException(String resourceName, String fieldName, String fieldValue) {
		super(resourceName+" Already Exists With "+fieldName+" : "+fieldValue+" !!");
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
}
