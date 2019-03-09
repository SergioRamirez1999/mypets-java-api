package com.sergio.apianimals.model.exceptions;

public class FamilyNotSavedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FamilyNotSavedException(String message) {
		super(message);
	}
	
}
