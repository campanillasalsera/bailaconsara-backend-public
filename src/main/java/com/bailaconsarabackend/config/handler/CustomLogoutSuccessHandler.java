package com.bailaconsarabackend.config.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que maneja el éxito del cierre de sesión personalizado.
 * 
 * Este manejador se encarga de proporcionar una respuesta adecuada al usuario
 * una vez que ha cerrado sesión correctamente.
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	/**
	 * Maneja el éxito del cierre de sesión del usuario.
	 *
	 * @param request        la solicitud HTTP
	 * @param response       la respuesta HTTP
	 * @param authentication la información de autenticación del usuario
	 * @throws IOException      si ocurre un error de E/S
	 * @throws ServletException si ocurre un error de servlet
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// Set the HTTP status code
		response.setStatus(HttpStatus.OK.value());

		// Write a message to the response
		response.getWriter().write("Sesión cerrada con éxito!");
		response.getWriter().flush();
	}

}
