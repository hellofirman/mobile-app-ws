package com.hellofirman.mobileappws.shared.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hellofirman.mobileappws.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	UserEntity findUserByEmail(String email);
	
}
