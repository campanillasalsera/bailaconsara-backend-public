package com.bailaconsarabackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad para almacenar tokens de autenticación.
 */
@Entity
@Table(name = "token")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String token;
	private boolean loggedout;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * Constructor que inicializa todos los campos.
	 *
	 * @param id        Identificador único.
	 * @param token     El token de autenticación.
	 * @param loggedout Indica si el token ha sido cerrado sesión.
	 * @param user      Usuario asociado al token.
	 */
	public Token(Long id, String token, boolean loggedout, User user) {
		this.id = id;
		this.token = token;
		this.loggedout = loggedout;
		this.user = user;
	}

	/**
	 * Constructor que crea un token solo con el valor del token.
	 *
	 * @param token El token de autenticación.
	 */
	public Token(String token) {
		this.token = token;
	}

	/**
	 * Constructor que crea un token con el valor del token y el usuario asociado.
	 *
	 * @param token El token de autenticación.
	 * @param user  Usuario asociado al token.
	 */
	public Token(String token, User user) {
		this.token = token;
		this.user = user;
	}

	/**
	 * Constructor predeterminado.
	 */
	public Token() {
	}

	/**
	 * Getters y Setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isLoggedout() {
		return loggedout;
	}

	public void setLoggedout(boolean loggedout) {
		this.loggedout = loggedout;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
