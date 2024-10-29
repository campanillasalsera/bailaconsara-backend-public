package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.NotificarParejaEvent;
import com.bailaconsarabackend.model.User_Taller;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Clase que escucha los eventos de NotificarParejaEvent y envía notificaciones
 * por correo electrónico.
 */
@Component
public class NotificarParejaEventListener implements ApplicationListener<NotificarParejaEvent> {

	private final EmailService emailService;

	/**
	 * Constructor de la clase NotificarParejaEventListener.
	 *
	 * @param emailService el servicio de correo electrónico a utilizar
	 */
	public NotificarParejaEventListener(EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Maneja el evento de NotificarParejaEvent y envía una notificación por correo
	 * electrónico.
	 *
	 * @param event el evento de NotificarParejaEvent
	 */
	@Override
	public void onApplicationEvent(NotificarParejaEvent event) {
		User_Taller userTaller = event.getUserTaller();
		String nombrePareja = event.getNombrePareja();
		String oldPartnerName = event.getOldPartnerName();
		String urlLogin = event.getApplicationUrl() + "/auth";
		urlLogin = "http://localhost:4200/auth";

		try {
			emailService.sendPartnerNotification(userTaller, oldPartnerName, nombrePareja, urlLogin);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
