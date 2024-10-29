package com.bailaconsarabackend.dto;

import java.time.LocalDate;

import com.bailaconsarabackend.model.User;

/**
 * Clase DTO para representar un usuario en la aplicación. Incluye información
 * básica del usuario como ID, nombre, apellidos, fecha de nacimiento, teléfono,
 * email y bailerol.
 */
public class UserDto {

	private Long id;

	private String nombre;

	private String apellidos;

	private LocalDate fechanacimiento;

	private String telefono;

	private String email;

	private String bailerol;

	private String role;

	private boolean isEnabled;

	private boolean isNotLocked;

	/**
	 * Constructor sin argumentos para UserDto.
	 */
	public UserDto() {
	}

	/**
	 * Constructor para crear una instancia de UserDto.
	 *
	 * @param id              el identificador único del usuario
	 * @param nombre          el nombre del usuario
	 * @param apellidos       los apellidos del usuario
	 * @param fechanacimiento la fecha de nacimiento del usuario
	 * @param telefono        el número de teléfono del usuario
	 * @param email           la dirección de correo electrónico del usuario
	 * @param bailerol        el rol de baile del usuario
	 * @param role            el rol del usuario en el sistema
	 * @param isEnabled       indica si el usuario está habilitado
	 * @param isNotLocked     indica si el usuario no está bloqueado
	 */
	public UserDto(Long id, String nombre, String apellidos, LocalDate fechanacimiento, String telefono, String email,
			String bailerol, String role, boolean isEnabled, boolean isNotLocked) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechanacimiento = fechanacimiento;
		this.telefono = telefono;
		this.email = email;
		this.bailerol = bailerol;
		this.role = role;
		this.isEnabled = isEnabled;
		this.isNotLocked = isNotLocked;
	}

	/**
	 * Constructor que toma un objeto User y lo convierte en un UserDto.
	 *
	 * @param user Objeto User a convertir.
	 */
	public UserDto(User user) {
		this.id = user.getId();
		this.nombre = user.getNombre();
		this.apellidos = user.getApellidos();
		this.fechanacimiento = user.getFechanacimiento();
		this.telefono = user.getTelefono();
		this.email = user.getEmail();
		this.bailerol = user.getBailerol();
		this.role = user.getRole().toString();
		this.isEnabled = user.isEnabled();
	}

	/*
	 * Getters y Setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getBailerol() {
		return bailerol;
	}

	public void setBailerol(String bailerol) {
		this.bailerol = bailerol;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isNotLocked() {
		return isNotLocked;
	}

	public void setNotLocked(boolean isNotLocked) {
		this.isNotLocked = isNotLocked;
	}

}
