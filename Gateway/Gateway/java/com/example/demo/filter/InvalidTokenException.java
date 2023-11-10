package com.example.demo.filter;

public class InvalidTokenException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidTokenException() {
		
	}
	
public InvalidTokenException(String msg) {
		super(msg);
	}

}
