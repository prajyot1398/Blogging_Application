package com.bloggingapi.exception;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidFieldException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String filedName;
	private String entity;
	private List<String> validFields;
	
	public InvalidFieldException(String fieldName, String entity , List<String> validFields) {
		super("Invalid Field : "+fieldName+" For Entity : "+entity+". Valid Fields : "+validFields);
		this.filedName = fieldName;
		this.entity = entity;
		this.validFields = validFields;
	}
	
}
