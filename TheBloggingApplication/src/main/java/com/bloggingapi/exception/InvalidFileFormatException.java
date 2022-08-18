package com.bloggingapi.exception;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidFileFormatException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String invalidFormat;
	private String entity;
	private List<String> validFormats;
	
	public InvalidFileFormatException(String invalidFormat, String entity, List<String> validFormats) {
		
		super("Invalid File Format : "+invalidFormat+" For "+entity+". Valid Formats : "+validFormats);
		this.invalidFormat = invalidFormat;
		this.entity = entity;
		this.validFormats = validFormats;
	}
}
