package com.bailaconsarabackend.event.listener;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.event.VerificarEmailRegistroEvent;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.repository.TokenRepository;
import com.bailaconsarabackend.service.EmailService;

import jakarta.mail.MessagingException;

/**
 * Componente que escucha eventos de solicitud de verificación de correo
 * electrónico durante el registro de usuarios. Cuando se dispara un evento de
 * solicitud de verificación de correo electrónico, este componente envía un
 * correo electrónico al usuario con un enlace para completar el proceso de
 * verificación.
 */
@Component
public class VerificarEmailRegistroEventListener implements ApplicationListener<VerificarEmailRegistroEvent> {

	/**
	 * Logger para registrar eventos y errores.
	 */
	private static final Logger log = LoggerFactory.getLogger(VerificarEmailRegistroEventListener.class);

	private final TokenRepository tokenRepository;
	private final EmailService emailService;

	/**
	 * Constructor para VerificarEmailRegistroEventListener.
	 *
	 * @param tokenRepository Repositorio de tokens.
	 * @param emailService    Servicio de correo electrónico.
	 */
	public VerificarEmailRegistroEventListener(TokenRepository tokenRepository, EmailService emailService) {
		this.tokenRepository = tokenRepository;
		this.emailService = emailService;
	}

	/**
	 * Método que se ejecuta cuando se dispara un evento de solicitud de
	 * verificación de correo electrónico. Envía un correo electrónico al usuario
	 * con un enlace para completar el proceso de verificación.
	 *
	 * @param event Evento de solicitud de verificación de correo electrónico.
	 */
	@Override
	public void onApplicationEvent(VerificarEmailRegistroEvent event) {
		if (event == null || event.getUser() == null) {
			throw new RuntimeException("evento Nulo");
		}

		// 1. coge el nuevo usuario registrado
		User user = event.getUser();

		// 2. Busca el token asociado al usuario
		String token = tokenRepository.findTokenByUser(user.getId())
				.orElseThrow(() -> new TokenNotFoundException("Token no encontrado para el usuario: " + user.getId()))
				.getToken();

		// 3. Construimos la url de verificacion para enviarla al usuario
		String url = event.getApplicationUrl() + "/auth/verifyEmail?token=" + token;

		// 4. Enviar el email
		try {
			emailService.sendVerificationEmail(user, url);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error en la codificación del correo electrónico", e);
		} catch (MessagingException e) {
			throw new RuntimeException("Error al enviar el correo electrónico de verificación", e);
		}
	}

}
