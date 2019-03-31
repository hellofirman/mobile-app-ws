package com.hellofirman.mobileappws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hellofirman.mobileappws.exceptions.UserServiceException;
import com.hellofirman.mobileappws.service.AddressService;
import com.hellofirman.mobileappws.service.UserService;
import com.hellofirman.mobileappws.shared.dto.AddressDto;
import com.hellofirman.mobileappws.shared.dto.UserDto;
import com.hellofirman.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.hellofirman.mobileappws.ui.model.response.AddressesRest;
import com.hellofirman.mobileappws.ui.model.response.ErrorMessagesEnum;
import com.hellofirman.mobileappws.ui.model.response.OperationStatusModel;
import com.hellofirman.mobileappws.ui.model.response.RequestOperationName;
import com.hellofirman.mobileappws.ui.model.response.RequestOperationStatus;
import com.hellofirman.mobileappws.ui.model.response.UserRest;


@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	//produces --> for return result with xml and json
	@GetMapping(
			path="/{userId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public UserRest getUser(@PathVariable String userId) {
		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(userId);
		
		//#1 - Mapper with beanUtils
		//BeanUtils.copyProperties(userDto, returnValue);
		
		//#2 - Mapper with modelmapper
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);
		
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
		
		//#1 - Mapper with beanUtils
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		//UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		
		//#2 - Mapper with modelmapper
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
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
	
	@DeleteMapping(
			path="/{userId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
		)
	public OperationStatusModel deleteUser(@PathVariable String userId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(userId);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	
	/*
	 * start with page = 0 
	 * max show = 10 records
	 * */ 
	@GetMapping(
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
		)
	public List<UserRest> getUsers(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {
		
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	// http://localhost:8080/mobile-app-ws/users/kfhvgiuycsdvhcbsdbcuy/addressses
	@GetMapping(
			path = "/{userId}/addresses", 
			produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE}
	)
	public List<AddressesRest> getUserAddresses(@PathVariable String userId) {
		
		List<AddressesRest> returnValue = new ArrayList<>();
		
		List<AddressDto> addressesDto = addressService.getAddresses(userId);
		
		if(addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}
		
		return returnValue;
	}
		
	// http://localhost:8080/mobile-app-ws/users/kfhvgiuycsdvhcbsdbcuy/addressses/asadaksdhaiogd8aygduas
	@GetMapping(path = "/{userId}/addresses/{addressId}", 
			produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }
	)
	public AddressesRest getOneUserAddress(@PathVariable String addressId) {

		AddressDto addressesDto = addressService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		
		return modelMapper.map(addressesDto, AddressesRest.class);
	}
		
}
