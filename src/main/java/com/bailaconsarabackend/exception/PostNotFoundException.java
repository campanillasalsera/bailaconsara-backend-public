package com.bailaconsarabackend.exception;

/**
 * Excepción lanzada cuando un post no puede ser encontrado.
 */
public class PostNotFoundException extends RuntimeException {

	/**
	 * Constructor de la excepción PostNotFoundException.
	 *
	 * @param message el mensaje de error
	 */
	public PostNotFoundException(String message) {
		super(message);
	}

}
