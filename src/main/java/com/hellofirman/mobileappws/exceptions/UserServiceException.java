package com.hellofirman.mobileappws.exceptions;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -4763053216429667825L;
	
	public UserServiceException (String message) {
		super(message);
	}

}
