package com.sergio.apianimals.model.exceptions;

public class AnimalBadFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnimalBadFormatException(String message) {
		super(message);
	}

}
