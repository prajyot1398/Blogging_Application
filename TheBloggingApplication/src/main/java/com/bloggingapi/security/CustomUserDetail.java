package com.bloggingapi.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bloggingapi.entity.User;

import lombok.Getter;

public class CustomUserDetail implements UserDetails{

	@Getter
    private User user;

    public CustomUserDetail(User user) {
        this.user = user;
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
			
		return this.user.getRoles().stream().map(
				(role)-> new SimpleGrantedAuthority(role.getRoleName()))
				.collect(Collectors.toSet());
	}
	@Override
	public String getPassword() {
		return this.user.getUserPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	} 
}
