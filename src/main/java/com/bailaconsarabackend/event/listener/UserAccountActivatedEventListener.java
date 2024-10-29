package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.UserAccountActivatedEvent;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Componente que escucha eventos de activación de cuentas de usuario. Cuando se
 * dispara un evento de activación de cuenta de usuario, este componente envía
 * una notificación por correo electrónico al usuario con la información de la
 * activación de su cuenta y un enlace para acceder a la aplicación.
 */
@Component
public class UserAccountActivatedEventListener implements ApplicationListener<UserAccountActivatedEvent> {

	/**
	 * Logger para registrar eventos y errores.
	 */
	private static final Logger log = LoggerFactory.getLogger(UserAccountActivatedEventListener.class);
	private final EmailService emailService;

	/**
	 * Constructor para UserAccountActivatedEventListener.
	 *
	 * @param emailService Servicio de correo electrónico.
	 */
	public UserAccountActivatedEventListener(EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Método que se ejecuta cuando se dispara un evento de activación de cuenta de
	 * usuario. Envía una notificación por correo electrónico al usuario con la
	 * información de la activación de su cuenta y un enlace para acceder a la
	 * aplicación.
	 *
	 * @param event Evento de activación de cuenta de usuario.
	 */
	@Async
	@Override
	public void onApplicationEvent(UserAccountActivatedEvent event) {

		User user = event.getUser();
		// url de verificacion para enviarla al usuario
		String urlLogin = event.getApplicationUrl() + "/auth";
		urlLogin = "https://bailaconsara.com";

		// Envia el email
		try {
			emailService.sendUserAccountActivatedNotification(user, urlLogin);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		log.info("Tu cuenta ha sido activada. Ya está activa. Inicia sesión : {}", urlLogin);

	}

}
