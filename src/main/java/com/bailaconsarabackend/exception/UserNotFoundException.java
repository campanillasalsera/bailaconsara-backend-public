package com.bailaconsarabackend.exception;

/**
 * Excepción lanzada cuando un usuario no puede ser encontrado.
 */
public class UserNotFoundException extends RuntimeException {

	/**
	 * Constructor de la excepción UserNotFoundException.
	 *
	 * @param message el mensaje de error
	 */
	public UserNotFoundException(String message) {
		super(message);
	}
}