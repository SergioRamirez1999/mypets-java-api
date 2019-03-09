package com.sergio.apianimals.model.exceptions;

public class AnimalNotSavedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnimalNotSavedException(String message) {
		super(message);
	}
	

}
