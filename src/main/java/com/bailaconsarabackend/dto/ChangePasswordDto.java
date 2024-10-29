package com.bailaconsarabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para cambiar
 * la contraseña.
 */
public class ChangePasswordDto {

	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Introduce una contraseña válida")
	private String password;

	@NotBlank(message = "Repite la contraseña, por favor")
	private String repeatPassword;

	/**
	 * Constructor por defecto de la clase.
	 */
	public ChangePasswordDto() {
	}

	/**
	 * Constructor con parámetros para inicializar la contraseña y su repetición.
	 *
	 * @param password       La nueva contraseña.
	 * @param repeatPassword La contraseña repetida para confirmación.
	 */
	public ChangePasswordDto(String password, String repeatPassword) {
		this.password = password;
		this.repeatPassword = repeatPassword;
	}

	/**
	 * Getters y Setters
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}
