package com.hellofirman.mobileappws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellofirman.mobileappws.exceptions.UserServiceException;
import com.hellofirman.mobileappws.service.UserService;
import com.hellofirman.mobileappws.shared.dto.UserDto;
import com.hellofirman.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.hellofirman.mobileappws.ui.model.response.ErrorMessagesEnum;
import com.hellofirman.mobileappws.ui.model.response.UserRest;


@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;
	
	//produces --> for return result with xml and json
	@GetMapping(
			path="/{userId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public UserRest getUser(@PathVariable String userId) {
		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}
	
	//consumes --> for recieve body with format xml and json
	@PostMapping(
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
		)
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty()) {
			// Default Exception Msg
			throw new Exception(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage() + " FirstName"); 
		}else if(userDetails.getLastName().isEmpty()) {
			// AppExceptionsHandler.handleUserServiceException
			throw new UserServiceException(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage() +  " LastName");
		}else if(userDetails.getEmail().isEmpty()) {
			// AppExceptionsHandler.handleOtherExceptions
			throw new NullPointerException("Email cannot be Empty");
		}
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping(
			path="/{userId}",
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
		)
	public UserRest updateUser(
			@PathVariable String userId, 
			@RequestBody UserDetailsRequestModel userDetails) {
		
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updateUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "delete users was called";
	}
	
}
