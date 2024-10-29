package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.NotificarSinParejaEvent;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.User_Taller;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Clase que escucha los eventos de NotificarSinParejaEvent y envía
 * notificaciones por correo electrónico.
 */
@Component
public class NotificarSinParejaEventListener implements ApplicationListener<NotificarSinParejaEvent> {

	private final EmailService emailService;

	/**
	 * Constructor de la clase NotificarSinParejaEventListener.
	 *
	 * @param emailService el servicio de correo electrónico a utilizar
	 */
	public NotificarSinParejaEventListener(EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Maneja el evento de NotificarSinParejaEvent y envía una notificación por
	 * correo electrónico.
	 *
	 * @param event el evento de NotificarSinParejaEvent
	 */
	@Override
	public void onApplicationEvent(NotificarSinParejaEvent event) {
		User_Taller userTaller = event.getUserTaller();
		Long userTallerId = userTaller.getId();
		User partner = event.getPartner();
		String urlLogIn = event.getApplicationUrl() + "auth";
		// dirección para pruebas
		urlLogIn = "http://localhost:4200/auth";

		try {
			emailService.sendNoPartnerNotification(userTaller, partner, urlLogIn);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
