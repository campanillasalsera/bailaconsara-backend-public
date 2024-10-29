package com.bailaconsarabackend.exception;

/**
 * Excepción lanzada cuando se intenta crear un usuario que ya existe.
 */
public class UserAlreadyExistsException extends Exception {

	/**
	 * Constructor de la excepción UserAlreadyExistsException.
	 *
	 * @param message el mensaje de error
	 */
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
