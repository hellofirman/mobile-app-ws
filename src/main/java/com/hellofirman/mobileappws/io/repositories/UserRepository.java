package com.hellofirman.mobileappws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hellofirman.mobileappws.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	UserEntity findUserByEmail(String email);
	
	// findBy (for select query) && UserId (name must same like user_id in database)
	UserEntity findByUserId(String userId);
}
