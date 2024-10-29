package com.bailaconsarabackend.event;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User_Taller;

/**
 * Evento que se dispara para notificar un cambio de pareja en un taller. Este
 * evento contiene información sobre el usuario asociado al taller, el antiguo y
 * el nuevo compañero de pareja, y la URL de la aplicación para realizar
 * acciones adicionales.
 */
public class NotificarParejaEvent extends ApplicationEvent {

	private User_Taller userTaller; // El usuario asociado al taller
	private String oldPartnerName; // El nombre del compañero de pareja anterior
	private String nombrePareja; // El nombre del nuevo compañero de pareja
	private String applicationUrl; // La URL de la aplicación

	/**
	 * Constructor de la clase NotificarParejaEvent.
	 *
	 * @param userTaller     el usuario asociado al taller
	 * @param oldPartnerName el nombre del compañero de pareja anterior
	 * @param nombrePareja   el nombre del nuevo compañero de pareja
	 * @param applicationUrl la URL de la aplicación
	 */
	public NotificarParejaEvent(User_Taller userTaller, String oldPartnerName, String nombrePareja,
			String applicationUrl) {
		super(userTaller);
		this.userTaller = userTaller;
		this.oldPartnerName = oldPartnerName;
		this.nombrePareja = nombrePareja;
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

	public String getNombrePareja() {
		return nombrePareja;
	}

	public void setNombrePareja(String nombrePareja) {
		this.nombrePareja = nombrePareja;
	}

	public String getOldPartnerName() {
		return oldPartnerName;
	}

	public void setOldPartnerName(String oldPartnerName) {
		this.oldPartnerName = oldPartnerName;
	}

}
