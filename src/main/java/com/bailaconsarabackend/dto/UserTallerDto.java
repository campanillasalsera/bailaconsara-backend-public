package com.bailaconsarabackend.dto;

import com.bailaconsarabackend.model.UserTallerEstado;

/**
 * Clase DTO (Data Transfer Object) que representa a un usuario en relación con
 * un taller. Esta clase se utiliza para transferir datos entre diferentes capas
 * de la aplicación, especialmente para enviar información sobre usuarios
 * participantes en talleres desde el backend hacia el frontend.
 */
public class UserTallerDto {

	private Long id;

	private String nombre;

	private String apellidos;

	private String telefono;

	private String email;

	private String bailerol;

	private UserTallerEstado user_taller_estado;

	private String userPartner;

	/**
	 * Constructor vacío de UserTallerDto.
	 */
	public UserTallerDto() {
	}

	/**
	 * Constructor de UserTallerDto que inicializa el objeto con todos los atributos
	 * necesarios.
	 *
	 * @param id                 identificador único del usuario en el contexto del
	 *                           taller.
	 * @param nombre             nombre del usuario.
	 * @param apellidos          apellidos del usuario.
	 * @param telefono           teléfono de contacto del usuario.
	 * @param email              correo electrónico del usuario.
	 * @param bailerol           rol o posición de baile del usuario dentro del
	 *                           taller.
	 * @param user_taller_estado estado actual del usuario en el taller.
	 * @param userPartner        información sobre el compañero de baile del usuario
	 *                           en el taller.
	 */
	public UserTallerDto(Long id, String nombre, String apellidos, String telefono, String email, String bailerol,
			UserTallerEstado user_taller_estado, String userPartner) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.email = email;
		this.bailerol = bailerol;
		this.user_taller_estado = user_taller_estado;
		this.userPartner = userPartner;
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

	public UserTallerEstado getUser_taller_estado() {
		return user_taller_estado;
	}

	public void setUser_taller_estado(UserTallerEstado user_taller_estado) {
		this.user_taller_estado = user_taller_estado;
	}

	public String getUserPartner() {
		return userPartner;
	}

	public void setUserPartner(String userPartner) {
		this.userPartner = userPartner;
	}

}
