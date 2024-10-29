package com.bailaconsarabackend.config.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Manejador personalizado para excepciones de acceso denegado.
 * 
 * Esta clase implementa la interfaz AccessDeniedHandler y se encarga de manejar
 * las excepciones que ocurren cuando un usuario intenta acceder a un recurso
 * sin tener los permisos adecuados. En lugar de simplemente devolver un error
 * 403, se envía una respuesta JSON que proporciona detalles sobre el error.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * Maneja una excepción de acceso denegado.
	 *
	 * @param request               la solicitud HTTP
	 * @param response              la respuesta HTTP
	 * @param accessDeniedException la excepción de acceso denegado
	 * @throws IOException      si ocurre un error de E/S
	 * @throws ServletException si ocurre un error de servlet
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		String responseBody = "{\"error\": \"Acceso denegado\", \"message\": \"" + accessDeniedException.getMessage()
				+ "\"}";
		response.getWriter().write(responseBody);
	}
}