package com.hellofirman.mobileappws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hellofirman.mobileappws.io.entity.UserEntity;
import com.hellofirman.mobileappws.io.repositories.UserRepository;
import com.hellofirman.mobileappws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userServiceImpl;
	
	@Mock
	UserRepository userRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		// initMocks is for @InjectMocks that usually we written userServiceImpl = new UserServiceImpl() or @Autowired
		MockitoAnnotations.initMocks(this); 
	}

	@Test
	void testGetUser() {
		//fail("Not yet implemented");
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Zaidin");
		userEntity.setUserId("edgf4yt6tkcgkjgcka");
		userEntity.setEncryptedPassword("uiytfresdfvghjksa8765");
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userServiceImpl.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Zaidin", userDto.getFirstName());
		
	}

}
