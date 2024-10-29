package com.bailaconsarabackend.dto;

import org.springframework.http.HttpStatus;

/**
 * Clase que representa un objeto de respuesta básico con los campos status,
 * mensaje y data.
 */
public class BasicResponseDto {

	private HttpStatus status;
	private String message;
	private Object data;
	private String url;

	/**
	 * Constructor vacío de la clase BasicResponseDto.
	 */
	public BasicResponseDto() {
	}

	/**
	 * Constructor de la clase BasicResponseDto con todos los campos: estado,
	 * mensaje, datos y url.
	 *
	 * @param status  El estado de la respuesta.
	 * @param message El mensaje de la respuesta.
	 * @param data    Los datos de la respuesta.
	 * @param url
	 */
	public BasicResponseDto(HttpStatus status, String message, Object data, String url) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.url = url;
	}

	/**
	 * Constructor de la clase BasicResponseDto con estado, mensaje, datos y url.
	 *
	 * @param status  El estado de la respuesta.
	 * @param message El mensaje de la respuesta.
	 * @param data    Los datos de la respuesta.
	 */
	public BasicResponseDto(HttpStatus status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
