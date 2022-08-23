package com.bloggingapi.security;

import lombok.Data;

@Data
public class JwtAuthRequest {
    
    private String userEmail;
    private String userPassword;
}
