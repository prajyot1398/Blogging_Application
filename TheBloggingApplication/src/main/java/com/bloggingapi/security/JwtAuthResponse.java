package com.bloggingapi.security;

import com.bloggingapi.payload.UserForm;

import lombok.Data;

@Data
public class JwtAuthResponse {
    
    private String token;
    private UserForm userForm;
}
