package com.bailaconsarabackend.exception;

/**
 * Excepción lanzada cuando un taller no puede ser encontrado.
 */
public class TallerNotFoundException extends Exception {

	/**
	 * Constructor de la excepción TallerNotFoundException.
	 *
	 * @param message el mensaje de error
	 */
	public TallerNotFoundException(String message) {
		super(message);
	}

}
