package com.bloggingapi.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bloggingapi.controller.UserController;
import com.bloggingapi.payload.UserForm;
import com.bloggingapi.payload.apiresponse.ApiResponse;
import com.bloggingapi.util.UserUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserController userController;
    
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
        @RequestBody JwtAuthRequest jwtAuthRequest) {

        this.authenticate(jwtAuthRequest.getUserEmail(), jwtAuthRequest.getUserPassword());
        
        UserDetails userDetails = this.userDetailService.loadUserByUsername(jwtAuthRequest.getUserEmail());
        String generatedToken = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(generatedToken);
        response.setUserForm(UserUtil.userToUserForm(((CustomUserDetail)userDetails).getUser()));
        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken 
        = new UsernamePasswordAuthenticationToken(username, password);
        
        try {
            this.authenticationManager.authenticate(authenticationToken);
        }catch(BadCredentialsException ex) {
            System.out.println("Invalid Credentials");
            throw new RuntimeException("Invalid Username Or Password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUpNewUser(
        @Valid @RequestBody UserForm userForm
    ) {
        return this.userController.registerUser(userForm);
    }
}
