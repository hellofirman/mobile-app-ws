package com.hellofirman.mobileappws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hellofirman.mobileappws.entity.UserEntity;
import com.hellofirman.mobileappws.service.UserService;
import com.hellofirman.mobileappws.shared.Utils;
import com.hellofirman.mobileappws.shared.dto.UserDto;
import com.hellofirman.mobileappws.shared.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;	
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		/* Changing message value for output 
		 * from  "message": "could not execute statement; SQL [n/a]; constraint [UK_6dotkott2kjsp8vw4d0m25fb7]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
		 * to be "message": "The users.email already exists"*/
		if(userRepository.findUserByEmail(user.getEmail()) != null) throw new RuntimeException("The users.email already exists"); 
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

}
