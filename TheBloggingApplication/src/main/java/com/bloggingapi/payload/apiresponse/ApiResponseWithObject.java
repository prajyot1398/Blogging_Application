package com.bloggingapi.payload.apiresponse;

import lombok.Getter;

@Getter
public class ApiResponseWithObject<A> extends ApiResponse {
	
	private final A responseObject;
	
	public ApiResponseWithObject(String message, boolean success, A object) {
		super(message, success);
		this.responseObject = object;
	}
}
