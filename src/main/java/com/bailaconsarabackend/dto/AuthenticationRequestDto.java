package com.bailaconsarabackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Clase que representa un objeto de solicitud de autenticación. Esta clase se
 * utiliza para encapsular los datos necesarios para autenticar a un usuario,
 * incluyendo el correo electrónico y la contraseña.
 */
public class AuthenticationRequestDto {

	@NotBlank(message = "El email no puede estar en blanco")
	@Email(message = "Introduce un email válido")
	private String email;

	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Introduce una contraseña válida")
	private String password;

	/**
	 * Constructor vacío de la clase AuthenticationRequestDto. Este constructor es
	 * útil para la creación de instancias sin necesidad de inicializar los
	 * atributos.
	 */
	public AuthenticationRequestDto() {
	}

	/**
	 * Constructor de la clase AuthenticationRequestDto.
	 *
	 * @param email    El email.
	 * @param password La contraseña.
	 */
	public AuthenticationRequestDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * Getters y Setters
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
