package com.sergio.apianimals.model.exceptions;

public class UserNotUpdatedException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UserNotUpdatedException(String message) {
		super(message);
	}

}
