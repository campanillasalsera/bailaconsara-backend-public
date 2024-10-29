package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.EmailVerifiedEvent;
import com.bailaconsarabackend.model.Token;
import com.bailaconsarabackend.repository.TokenRepository;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Componente que escucha eventos de verificación de correo electrónico de
 * usuarios. Cuando se dispara un evento de verificación de correo electrónico,
 * este componente envía una notificación por correo electrónico a un
 * administrador con la información del usuario y un enlace para activar su
 * cuenta.
 */
@Component
public class EmailVerifiedEventListener implements ApplicationListener<EmailVerifiedEvent> {

	/**
	 * Logger para registrar eventos y errores.
	 */
	private static final Logger log = LoggerFactory.getLogger(EmailVerifiedEventListener.class);

	private final EmailService emailService;
	private final TokenRepository tokenRepository;

	/**
	 * Constructor para EmailVerifiedEventListener.
	 *
	 * @param emailService    Servicio de correo electrónico.
	 * @param tokenRepository Repositorio de tokens.
	 */
	public EmailVerifiedEventListener(EmailService emailService, TokenRepository tokenRepository) {
		this.emailService = emailService;
		this.tokenRepository = tokenRepository;
	}

	/**
	 * Método que se ejecuta cuando se dispara un evento de verificación de correo
	 * electrónico. Envía una notificación por correo electrónico a un administrador
	 * con la información del usuario y un enlace para activar su cuenta.
	 *
	 * @param event Evento de verificación de correo electrónico.
	 */
	@Async
	@Override
	public void onApplicationEvent(EmailVerifiedEvent event) {
		String userName = event.getUser().getNombre() + " " + event.getUser().getApellidos();
		String adminName = "Sara";
		Token theToken = new Token();
		try {
			theToken = tokenRepository.findTokenByUser(event.getUser().getId())
					.orElseThrow(() -> new NotFoundException());
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String token = theToken.getToken();
		String url = event.getApplicationUrl() + "/auth/verifyNewUserAccount?token=" + token;

		try {
			emailService.sendAdminNotification(userName, url, adminName);
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("Haz click en el enlace para activar usuario : {}", url);
	}

}
