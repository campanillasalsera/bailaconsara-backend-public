package com.bailaconsarabackend.event;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User;

/**
 * Evento personalizado para notificar cuando se solicita la verificación de
 * correo electrónico durante el registro de un usuario. Contiene información
 * sobre el usuario y la URL de la aplicación.
 */
public class VerificarEmailRegistroEvent extends ApplicationEvent {

	private User user;
	private String applicationUrl;

	/**
	 * Constructor para VerificarEmailRegistroEvent.
	 *
	 * @param user           Usuario que solicita la verificación de correo
	 *                       electrónico.
	 * @param applicationUrl URL de la aplicación donde se solicita la verificación
	 *                       de correo electrónico.
	 */
	public VerificarEmailRegistroEvent(User user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}

	/**
	 * Getters y Setters
	 */
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}

}
