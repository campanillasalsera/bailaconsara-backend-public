package com.bailaconsarabackend.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utilidad para obtener la URL base de la aplicación.
 */
@Component
public class ApplicationUrlUtil {

	/**
	 * Obtiene la URL base de la aplicación utilizando la información del
	 * servletRequest.
	 * 
	 * @param servletRequest Objeto HttpServletRequest que proporciona información
	 *                       sobre la solicitud HTTP.
	 * @return String que representa la URL base de la aplicación.
	 */
	public String applicationUrl(HttpServletRequest servletRequest) {
		return "https://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort()
				+ servletRequest.getContextPath();
	}

}
