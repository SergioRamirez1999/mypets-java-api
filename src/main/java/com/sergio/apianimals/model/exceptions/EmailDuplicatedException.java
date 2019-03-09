package com.sergio.apianimals.model.exceptions;

public class EmailDuplicatedException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public EmailDuplicatedException(String message) {
		super(message);
	}
}
