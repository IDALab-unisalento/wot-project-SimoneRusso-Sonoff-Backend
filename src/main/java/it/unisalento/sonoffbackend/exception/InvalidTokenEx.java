package it.unisalento.sonoffbackend.exception;

public class InvalidTokenEx extends Exception {
	String message;
	public InvalidTokenEx(String message) {
		super();
		this.message = message;
	}

}
