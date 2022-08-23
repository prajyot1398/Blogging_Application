package com.bloggingapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bloggingapi.entity.User;
import com.bloggingapi.exception.ResourceNotFoundException;
import com.bloggingapi.repository.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//loading user from database based on username. 
		User user = this.userRepo.findByUserEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
		
		return new CustomUserDetail(user);
	}

}
