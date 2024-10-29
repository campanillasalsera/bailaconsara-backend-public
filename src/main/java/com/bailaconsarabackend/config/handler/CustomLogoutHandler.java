package com.bailaconsarabackend.config.handler;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.bailaconsarabackend.model.Token;
import com.bailaconsarabackend.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que maneja el proceso de cierre de sesión personalizado.
 * 
 * Este manejador se encarga de invalidar el token de autenticación del usuario
 * en el momento en que se cierra la sesión. Al cerrar sesión, se actualiza el
 * estado del token en la base de datos para reflejar que el usuario ha cerrado
 * sesión.
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

	private final TokenRepository tokenRepository;

	/**
	 * Constructor de la clase CustomLogoutHandler.
	 *
	 * @param tokenRepository el repositorio de tokens a utilizar
	 */
	public CustomLogoutHandler(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	/**
	 * Maneja el proceso de cierre de sesión del usuario.
	 *
	 * @param request        la solicitud HTTP
	 * @param response       la respuesta HTTP
	 * @param authentication la información de autenticación del usuario
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null || authHeader.startsWith("Bearer ")) {

			String token = authHeader.substring(7);

			// coge el token guardado en la BD
			Optional<Token> optionalToken = tokenRepository.findByToken(token);
			Token storedToken = optionalToken.get();

			// invalida el token poniendo el loggout en true
			if (token != null) {
				storedToken.setLoggedout(true);
				tokenRepository.save(storedToken);
			}
		}
	}

}
