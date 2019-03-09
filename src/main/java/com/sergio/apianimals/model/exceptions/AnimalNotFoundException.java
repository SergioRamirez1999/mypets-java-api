package com.sergio.apianimals.model.exceptions;

public class AnimalNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnimalNotFoundException(String message) {
		super(message);
	}

}
