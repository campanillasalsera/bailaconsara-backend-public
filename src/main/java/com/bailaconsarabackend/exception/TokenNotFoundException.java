package com.bailaconsarabackend.exception;

/**
 * Excepción lanzada cuando un token no se encuentra o no es válido.
 */
public class TokenNotFoundException extends RuntimeException {

	/**
	 * Constructor de TokenNotFoundException que acepta un mensaje.
	 * 
	 * @param message el mensaje que describe la excepción
	 */
	public TokenNotFoundException(String message) {
		super(message);
	}
}
