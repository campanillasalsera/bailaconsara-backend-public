package com.bailaconsarabackend.exception;

import org.springframework.http.HttpStatus;

/**
 * Clase que representa un mensaje de error con un código de estado y un
 * mensaje.
 */
public class ErrorMessage {

	private HttpStatus status;
	private String message;

	/**
	 * Constructor por defecto de la clase ErrorMessage.
	 */
	public ErrorMessage() {
	}

	/**
	 * Constructor de la clase ErrorMessage con un código de estado y un mensaje.
	 *
	 * @param status  el código de estado HTTP
	 * @param message el mensaje de error
	 */
	public ErrorMessage(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * Constructor de la clase ErrorMessage solo con un mensaje.
	 *
	 * @param message el mensaje de error
	 */
	public ErrorMessage(String message) {
		this.message = message;
	}

	/**
	 * Getters y Setters
	 */
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
