package com.bailaconsarabackend.event;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.User_Taller;

/**
 * Evento que se dispara para notificar que un usuario no tiene pareja en un
 * taller. Este evento contiene información sobre el usuario asociado al taller,
 * el compañero de pareja (si existe) y la URL de la aplicación para realizar
 * acciones adicionales.
 */
public class NotificarSinParejaEvent extends ApplicationEvent {

	private User_Taller userTaller; // El usuario asociado al taller
	private User partner; // El compañero de pareja, si existe
	private String applicationUrl; // La URL de la aplicación

	/**
	 * Constructor de la clase NotificarSinParejaEvent.
	 *
	 * @param userTaller     el usuario asociado al taller
	 * @param partner        el compañero de pareja
	 * @param applicationUrl la URL de la aplicación
	 */
	public NotificarSinParejaEvent(User_Taller userTaller, User partner, String applicationUrl) {
		super(userTaller);
		this.userTaller = userTaller;
		this.partner = partner;
		this.applicationUrl = applicationUrl;
	}

	/*
	 * Getters y Setters
	 */
	public User_Taller getUserTaller() {
		return userTaller;
	}

	public void setUserTaller(User_Taller userTaller) {
		this.userTaller = userTaller;
	}

	public User getPartner() {
		return partner;
	}

	public void setPartner(User partner) {
		this.partner = partner;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}

}
