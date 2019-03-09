package com.sergio.apianimals.model.exceptions;

public class UserBadFormatException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UserBadFormatException(String message) {
		super(message);
	}
}
