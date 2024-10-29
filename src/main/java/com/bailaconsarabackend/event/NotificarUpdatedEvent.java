package com.bailaconsarabackend.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.bailaconsarabackend.model.User_Taller;

/**
 * Evento que se dispara para notificar que un taller ha sido actualizado. Este
 * evento contiene información sobre el nombre del taller, el usuario asociado y
 * las modificaciones realizadas en el taller.
 */
public class NotificarUpdatedEvent extends ApplicationEvent {

	private String tallerNombre; // El nombre del taller actualizado
	private User_Taller userTaller; // El usuario asociado al taller
	private List<String> modificaciones; // Lista de modificaciones realizadas en el taller

	/**
	 * Constructor de la clase NotificarUpdatedEvent.
	 *
	 * @param tallerNombre   el nombre del taller actualizado
	 * @param userTaller     el usuario asociado al taller
	 * @param modificaciones la lista de modificaciones realizadas en el taller
	 */
	public NotificarUpdatedEvent(String tallerNombre, User_Taller userTaller, List<String> modificaciones) {
		super(userTaller);
		this.tallerNombre = tallerNombre;
		this.userTaller = userTaller;
		this.modificaciones = modificaciones;
	}

	/**
	 * Getters y Setters
	 */
	public String getTallerNombre() {
		return tallerNombre;
	}

	public void setTallerNombre(String tallerNombre) {
		this.tallerNombre = tallerNombre;
	}

	public User_Taller getUserTaller() {
		return userTaller;
	}

	public void setUserTaller(User_Taller userTaller) {
		this.userTaller = userTaller;
	}

	public List<String> getModificaciones() {
		return modificaciones;
	}

	public void setModificaciones(List<String> modificaciones) {
		this.modificaciones = modificaciones;
	}

}
