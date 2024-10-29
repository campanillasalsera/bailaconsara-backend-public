package com.bailaconsarabackend.event;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User;

/**
 * Evento personalizado para notificar cuando un usuario ha verificado su correo
 * electrónico. Contiene información sobre el usuario y la URL de la aplicación.
 */
public class EmailVerifiedEvent extends ApplicationEvent {

	private User user;
	private String applicationUrl;

	/**
	 * Constructor para EmailVerifiedEvent.
	 *
	 * @param user           Usuario que ha verificado su correo electrónico.
	 * @param applicationUrl URL de la aplicación donde se realizó la verificación.
	 */
	public EmailVerifiedEvent(User user, String applicationUrl) {
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
