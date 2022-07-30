package com.bloggingapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloggingapi.entity.User;

//This will add Normal CRUD methods of JPA directly and don't need to write the code for same.
//JpaRepository<T, ID>	:- T is the Entity class and ID is class/data type of primary key.


public interface UserRepo extends JpaRepository<User, Integer> {

	Optional<User> findByUserEmail(String userEmail);
	
	boolean existsUserByUserEmail(String userEmail); 
}
