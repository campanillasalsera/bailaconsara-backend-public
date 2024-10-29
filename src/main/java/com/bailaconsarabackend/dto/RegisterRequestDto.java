package com.bailaconsarabackend.dto;

import java.time.LocalDate;

import com.bailaconsarabackend.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Clase DTO para manejar la solicitud de registro de usuarios. Incluye
 * información básica del usuario como nombre, apellidos, fecha de nacimiento,
 * teléfono, email y contraseña.
 */
public class RegisterRequestDto {

	@Size(min = 2, message = "El nombre debe tener al menos 2 letras")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+( [a-zA-ZÀ-ÿ\\u00f1\\u00d1]+)*$", message = "El nombre solo puede contener letras y espacios")
	private String nombre;

	private String apellidos;

	@Past(message = "Introduce una fecha válida")
	@NotNull(message = "La fecha de nacimiento no puede estar en blanco")
	private LocalDate fechanacimiento;

	@NotBlank(message = "El teléfono no puede estar en blanco")
	@Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "El campo debe contener entre 6 y 15 dígitos numéricos, y puede comenzar con un '+'.")
	private String telefono;

	@NotBlank(message = "El email no puede estar en blanco")
	@Email(message = "Introduce un email válido")
	private String email;

	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!¡*@$%^&+=._-])(?=\\S+$).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una letra minúscula, un número y un carácter especial (!¡*@$%^&+=._-), sin espacios.")
	private String password;

	private String bailerol;

	private Role role;

	/**
	 * Constructor sin argumentos para RegisterRequestDto.
	 */
	public RegisterRequestDto() {
		super();
	}

	/**
	 * Constructor con todos los campos para RegisterRequestDto.
	 *
	 * @param nombre          Nombre del usuario.
	 * @param apellidos       Apellidos del usuario.
	 * @param fechanacimiento Fecha de nacimiento del usuario.
	 * @param telefono        Teléfono del usuario.
	 * @param email           Email del usuario.
	 * @param password        Contraseña del usuario.
	 * @param bailerol        Bailerol del usuario.
	 * @param role            Rol del usuario.
	 */
	public RegisterRequestDto(String nombre, String apellidos, LocalDate fechanacimiento, String telefono, String email,
			String password, String bailerol, Role role) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechanacimiento = fechanacimiento;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
		this.bailerol = bailerol;
		this.role = role;
	}

	/**
	 * Getters y Setters
	 */
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public LocalDate getFechanacimiento() {
		return fechanacimiento;
	}

	public void setFechanacimiento(LocalDate fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

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

	public String getBailerol() {
		return bailerol;
	}

	public void setBailerol(String bailerol) {
		this.bailerol = bailerol;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
