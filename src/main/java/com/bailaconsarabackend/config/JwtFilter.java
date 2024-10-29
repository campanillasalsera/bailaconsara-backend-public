package com.bailaconsarabackend.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bailaconsarabackend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticación JWT para la aplicación Bailaconsara.
 * 
 * Este filtro intercepta las solicitudes entrantes y verifica la validez del
 * token JWT. Si el token es válido, establece el contexto de seguridad con la
 * información del usuario autenticado.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	/**
	 * Constructor de la clase JwtFilter.
	 *
	 * @param userDetailsService el servicio de detalles de usuario a utilizar
	 * @param jwtService         el servicio JWT a utilizar
	 */
	public JwtFilter(UserDetailsService userDetailsService, JwtService jwtService) {
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}

	/**
	 * Implementación del método doFilterInternal de OncePerRequestFilter. Este
	 * método realiza la lógica de filtrado para verificar y procesar el token JWT
	 * en las solicitudes entrantes.
	 *
	 * @param request     la solicitud HTTP entrante
	 * @param response    la respuesta HTTP
	 * @param filterChain el filtro de cadena para continuar con la ejecución de
	 *                    otros filtros
	 * @throws ServletException si ocurre un error de servlet
	 * @throws IOException      si ocurre un error de E/S
	 */
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// quita la palabra Bearer y el espacio. 7 caracteres
		jwt = authHeader.substring(7);
		// obtenemos el email del servicio de jwt
		userEmail = jwtService.getUserName(jwt);
		// valida que el usuario no esté ya autenticado
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Busca en BD por el email usando un @Bean que tenemos que crear
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			// Valida que el token sea valido a traves del servicio
			try {
				if (jwtService.validateToken(jwt, userDetails)) {
					// Le pasamos las credenciales como nulas
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					// Creamos los detalles donde especificamos que los detalles vienen dentro del
					// request
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("El enlace ha expirado. Por favor, solicita uno nuevo.");
				e.printStackTrace();
			}
		}
		filterChain.doFilter(request, response);

	}

}