package com.bailaconsarabackend.dto;

/**
 * Clase que representa un objeto de respuesta de autenticación con los campos
 * token y mensaje.
 */
public class AuthResponseDto {

	private String token;
	private String mensaje;

	/**
	 * Constructor vacío de la clase AuthResponseDto.
	 */
	public AuthResponseDto() {
	}

	/**
	 * Constructor de la clase AuthResponseDto con todos los campos.
	 *
	 * @param token   El token generado.
	 * @param mensaje El mensaje de respuesta.
	 */
	public AuthResponseDto(String token, String mensaje) {
		this.token = token;
		this.mensaje = mensaje;
	}

	/**
	 * Constructor de la clase AuthResponseDto con token.
	 *
	 * @param token El token generado.
	 */
	public AuthResponseDto(String token) {
		this.token = token;
	}

	/**
	 * Getters y Setters
	 */
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
