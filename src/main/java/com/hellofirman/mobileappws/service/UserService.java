package com.hellofirman.mobileappws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.hellofirman.mobileappws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto updateUser(String userId, UserDto userDto);
}
