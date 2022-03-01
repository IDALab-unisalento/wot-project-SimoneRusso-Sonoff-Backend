package it.unisalento.sonoffbackend.exception;

public class InvalidTokenEx extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	public InvalidTokenEx(String message) {
		super();
		this.message = message;
	}

}
