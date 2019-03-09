package com.sergio.apianimals.model.exceptions;

public class FamilyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FamilyNotFoundException(String message) {
		super(message);
	}

}
