package com.bloggingapi.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bloggingapi.payload.apiresponse.ApiResponse;
import com.bloggingapi.payload.apiresponse.ApiResponseWithObject;

import lombok.val;

//Used for global exception handler and this annotation will scan all the controllers and
//Will be reached here if exception occurs in those controllers. 
@RestControllerAdvice
public class GlobalExceptionHandler {

		
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exp) {
		return new ResponseEntity<ApiResponse>(new ApiResponse(exp.getMessage(), false), 
					HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exp) {
		
		Map<String, String> exceptions = new HashMap<>();
		
		exp.getBindingResult().getAllErrors().forEach((error) -> {
			
			exceptions.put(((FieldError)error).getField(), error.getDefaultMessage());
		});
		
		return new ResponseEntity<ApiResponse>(new ApiResponseWithObject("Invalid Fields !!", false, exceptions), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidFieldException.class) 
	public ResponseEntity<ApiResponse> invalidFieldException(InvalidFieldException exp) {
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(exp.getMessage(), false), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ElementAlreadyExistException.class)
	public ResponseEntity<ApiResponse> elementAlreadyExistException(ElementAlreadyExistException exp) {
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(exp.getMessage(), false), HttpStatus.BAD_REQUEST);
	}
}
