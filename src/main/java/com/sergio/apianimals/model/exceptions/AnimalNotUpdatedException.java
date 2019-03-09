package com.sergio.apianimals.model.exceptions;

public class AnimalNotUpdatedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnimalNotUpdatedException(String message) {
		super(message);
	}
	
}
