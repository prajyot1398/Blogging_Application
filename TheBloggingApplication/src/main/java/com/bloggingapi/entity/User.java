package com.bloggingapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="blogging_users")
@NoArgsConstructor
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer userId;
	
	@Column(name="name", nullable = false, length = 100)
	private String userName;
	
	@Column(name="email", unique = true, nullable = false, length = 100)
	private String userEmail;
	
	@Column(name="about")
	private String userAbout;
	
	@Column(name="password")
	private String userPassword;
}
