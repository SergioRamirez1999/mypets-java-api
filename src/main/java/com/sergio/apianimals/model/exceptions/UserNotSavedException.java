package com.sergio.apianimals.model.exceptions;

public class UserNotSavedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UserNotSavedException(String message) {
		super(message);
	}

}
