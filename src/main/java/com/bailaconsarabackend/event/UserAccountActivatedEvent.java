package com.bailaconsarabackend.event;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User;

/**
 * Evento personalizado para notificar cuando una cuenta de usuario ha sido
 * activada. Contiene información sobre el usuario y la URL de la aplicación.
 */
public class UserAccountActivatedEvent extends ApplicationEvent {

	private User user;
	private String applicationUrl;

	/**
	 * Constructor para UserAccountActivatedEvent.
	 *
	 * @param user           Usuario cuya cuenta ha sido activada.
	 * @param applicationUrl URL de la aplicación donde se realizó la activación de
	 *                       la cuenta.
	 */
	public UserAccountActivatedEvent(User user, String applicationUrl) {
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
