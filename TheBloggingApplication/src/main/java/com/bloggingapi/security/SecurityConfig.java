package com.bloggingapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {//extends  WebSecurityConfigurerAdapter {
	
	public static final String[] PUBLIC_URLS = {
		"/api/auth/**",
		"/v3/api-docs",	//Fixed URL for Swagger
		"/v2/api-docs",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/webjars/**"
	};

	@Autowired
	private CustomUserDetailService userDetailService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeHttpRequests()
		.antMatchers(PUBLIC_URLS).permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.authenticationProvider(daoAuthenticationProvider());

		return http.build();
	}
	
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http
		.csrf().disable()
		.authorizeHttpRequests()
		.antMatchers(PUBLIC_URLS).permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}*/

	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		auth.userDetailsService(this.userDetailService).passwordEncoder(passwordEncoder());
	}*/
	
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	/*@Bean
	public AuthenticationManager authenticationManagerbean() throws Exception {
		return super.authenticationManagerBean();
	}*/
	
	@Bean
	public AuthenticationManager authenticationManagerbean(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
}
