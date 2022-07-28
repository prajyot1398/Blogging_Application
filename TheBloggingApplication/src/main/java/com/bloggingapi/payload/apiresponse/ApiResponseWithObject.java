package com.bloggingapi.payload.apiresponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseWithObject extends ApiResponse {
	
	private Object object;
	
	public ApiResponseWithObject(String message, boolean success, Object object) {
		super(message, success);
		this.object = object;
	}
}
