package com.bloggingapi.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {
    
    //Validity in milliseconds
    @Value("${jwt.tokenValidityInMs}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secretkey}")
    private String SECRET_KEY;

    //Retrieves username from JWT token 
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //Retrieves Expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    //Retrive any information from token.
    //We will need Secret Key.
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token).getBody();
    }
    
    //Check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    //Generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        /* Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			claims.put("isUser", true);
		} */
        return doGenerateToken(claims, userDetails.getUsername()); 
    }

    /**
     * While Creating the token.
     * 1. Define claims of the token, like Issuer, Expiration, Subject and the ID.
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-j...) 
     * compaction of the JWT to a URL-safe string 
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
    }

    //Validate Token
    public Boolean validateToken(String token, UserDetails userDetails) {

        final String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
    }
}   
