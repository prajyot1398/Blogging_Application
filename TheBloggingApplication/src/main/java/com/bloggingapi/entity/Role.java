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
@Table(name = "blogging_roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "role_name")
	private String roleName;

	//@ManyToMany(mappedBy = "user", fetch = FetchType.LAZY)
	//private Set<User> users = new HashSet<>();

	public Role(String roleName) {
		this.roleName = roleName;
	}
}
