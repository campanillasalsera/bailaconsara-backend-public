package com.bailaconsarabackend.exception;

/**
 * Excepción lanzada cuando un post no cumple las reglas de validación.
 */
public class PostValidationFailedException extends Exception {

	/**
	 * Constructor de la excepción PostValidationFailedException.
	 *
	 * @param message el mensaje de error
	 */
	public PostValidationFailedException(String message) {
		super(message);
	}

}
