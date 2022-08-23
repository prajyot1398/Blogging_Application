package com.bloggingapi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        //1. Get Token   
        String token = request.getHeader("Authorization");
        
        //Bearer x.y.z
        //2. Get Username
        System.out.println(token);
        String userName = null;
        if(token != null && token.startsWith("Bearer")) {

            token = token.substring(7);
            try {
                userName = this.jwtTokenHelper.getUsernameFromToken(token);
            }catch(IllegalArgumentException | SignatureException | MalformedJwtException | UnsupportedJwtException ex) {
                System.out.println(ex);
            } catch(ExpiredJwtException ex) {
                System.out.println(ex);
            }
        } else {
            System.out.println("JWT Token Is Null Or Doesn't Start With Bearer");
        }       

        //3. Validate token
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =  this.userDetailsService.loadUserByUsername(userName);
            if(this.jwtTokenHelper.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else {
                System.out.println("Invalid JWT Token");
            }
        } else {
            System.out.println("No Username In The Token");
        }
         
        filterChain.doFilter(request, response);

    }
    
}
