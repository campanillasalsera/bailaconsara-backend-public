package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.NotificarUpdatedEvent;
import com.bailaconsarabackend.model.User_Taller;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Clase que escucha los eventos de NotificarUpdatedEvent y envía notificaciones
 * por correo electrónico.
 */
@Component
public class NotificarUpdatedEventListener implements ApplicationListener<NotificarUpdatedEvent> {

	private final EmailService emailService;

	/**
	 * Constructor de la clase NotificarUpdatedEventListener.
	 *
	 * @param emailService el servicio de correo electrónico a utilizar
	 */
	public NotificarUpdatedEventListener(EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Maneja el evento de NotificarUpdatedEvent y envía una notificación por correo
	 * electrónico.
	 *
	 * @param event el evento de NotificarUpdatedEvent
	 */
	@Override
	public void onApplicationEvent(NotificarUpdatedEvent event) {
		User_Taller userTaller = event.getUserTaller();
		String tallerNombre = event.getTallerNombre();
		List<String> modificaciones = event.getModificaciones();
		try {
			emailService.sendUpdatedEventNotification(tallerNombre, userTaller, modificaciones);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
