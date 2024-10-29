package com.bailaconsarabackend.event;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.User_Taller;

/**
 * Evento que se dispara para notificar la creación de una nueva pareja en un
 * taller. Este evento contiene información sobre el usuario asociado al taller,
 * su nuevo compañero y la URL de la aplicación para realizar acciones
 * adicionales.
 */
public class NotificarNuevaParejaEvent extends ApplicationEvent {

	private User_Taller userTaller; // El usuario asociado al taller
	private User partner; // El nuevo compañero de pareja
	private String applicationUrl; // La URL de la aplicación

	/**
	 * Constructor de la clase NotificarNuevaParejaEvent.
	 *
	 * @param userTaller     el usuario asociado al taller
	 * @param partner        el nuevo compañero de pareja
	 * @param applicationUrl la URL de la aplicación
	 */
	public NotificarNuevaParejaEvent(User_Taller userTaller, User partner, String applicationUrl) {
		super(userTaller);
		this.userTaller = userTaller;
		this.partner = partner;
		this.applicationUrl = applicationUrl;
	}

	/*
	 * Getters y Setters
	 */
	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}

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

}
