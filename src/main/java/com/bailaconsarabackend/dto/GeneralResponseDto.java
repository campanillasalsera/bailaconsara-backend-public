package com.bailaconsarabackend.dto;

import org.springframework.http.HttpStatus;

/**
 * Clase que representa un objeto de respuesta general con los campos status y
 * mensaje.
 */
public class GeneralResponseDto {

	private HttpStatus status;
	private String message;

	/**
	 * Constructor por defecto de la clase.
	 */
	public GeneralResponseDto() {
	}

	/**
	 * Constructor con parámetros para inicializar el estado y el mensaje de la
	 * respuesta.
	 *
	 * @param status  El estado HTTP de la respuesta.
	 * @param message El mensaje de la respuesta.
	 */
	public GeneralResponseDto(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

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
