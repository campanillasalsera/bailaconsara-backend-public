package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.NotificarNuevaParejaEvent;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.User_Taller;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Clase que escucha los eventos de NotificarNuevaParejaEvent y envía
 * notificaciones por correo electrónico.
 */
@Component
public class NotificarNuevaParejaEventListener implements ApplicationListener<NotificarNuevaParejaEvent> {

	private final EmailService emailService;

	/**
	 * Constructor de la clase NotificarNuevaParejaEventListener.
	 *
	 * @param emailService el servicio de correo electrónico a utilizar
	 */
	public NotificarNuevaParejaEventListener(EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Maneja el evento de NotificarNuevaParejaEvent y envía una notificación por
	 * correo electrónico.
	 *
	 * @param event el evento de NotificarNuevaParejaEvent
	 */
	@Override
	public void onApplicationEvent(NotificarNuevaParejaEvent event) {
		User_Taller userTaller = event.getUserTaller();
		Long userTallerId = userTaller.getId();
		User partner = event.getPartner();

		String urlLogin = event.getApplicationUrl() + "/auth";

		urlLogin = "http://localhost:4200/auth";

		try {
			emailService.sendNewPartnerNotification(userTaller, partner, urlLogin);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
