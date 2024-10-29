package com.bailaconsarabackend.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.event.UserAccountActivatedEvent;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.Token;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.repository.TokenRepository;
import com.bailaconsarabackend.repository.UserRepository;
import com.bailaconsarabackend.service.AdminService;
import com.bailaconsarabackend.util.ApplicationUrlUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Implementación del servicio de administración que define los métodos
 * administrativos para la gestión de usuarios en el sistema. Ofrece operaciones
 * para la actualización de roles, activación y desactivación de cuentas, y
 * verificación de nuevas cuentas de usuario.
 */
@Service
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final ApplicationUrlUtil applicationUrlUtil;

	/**
	 * Constructor para la clase AdminServiceImpl.
	 *
	 * @param userRepository     Repositorio de usuarios.
	 * @param tokenRepository    Repositorio de tokens.
	 * @param eventPublisher     Publicador de eventos de la aplicación.
	 * @param applicationUrlUtil Utilidad para construir URLs de la aplicación,
	 *                           utilizada para generar URLs dinámicas basadas en la
	 *                           configuración del servidor.
	 */
	public AdminServiceImpl(UserRepository userRepository, TokenRepository tokenRepository,
			ApplicationEventPublisher eventPublisher, ApplicationUrlUtil applicationUrlUtil) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.eventPublisher = eventPublisher;
		this.applicationUrlUtil = applicationUrlUtil;
	}

	/**
	 * Verifica la cuenta de un nuevo usuario utilizando el token proporcionado. Es
	 * usado una vez que el usuario a verificado su email, el administrador recibe
	 * un email para aprobar la cuenta del usuario (la desbloquea)
	 *
	 * @param stringToken   El token de verificación.
	 * @param serverRequest La solicitud HTTP recibida.
	 * @return Un mensaje indicando el resultado de la verificación.
	 * @throws TokenNotFoundException
	 */
	@Override
	public ModelAndView verifyNewUserAccount(String stringToken, HttpServletRequest serverRequest)
			throws TokenNotFoundException {
		ModelAndView modelAndView = new ModelAndView("verifyEmailResult");
		String resultadoValidacion = "Activación fallida";
		if (stringToken == null || stringToken.isBlank()) {
			resultadoValidacion = "Token inválido";
		} else {

			Token token = tokenRepository.findByToken(stringToken)
					.orElseThrow(() -> new TokenNotFoundException("Token no encontrado"));
			User user = token.getUser();
			boolean isHabilitada = user.isEnabled();
			boolean isDesbloqueada = user.isNotLocked();
			String url = applicationUrlUtil.applicationUrl(serverRequest);

			if (!isHabilitada) {
				resultadoValidacion = "¡Esta cuenta no ha sido verificada aún!";
			} else {
				if (isDesbloqueada) {
					resultadoValidacion = "¡Esta cuenta ya ha sido desbloqueada!";
				} else {
					try {
						String verificationResult = this.desbloquearUserAccount(token);
						if (verificationResult.equalsIgnoreCase("desbloqueado")) {
							resultadoValidacion = "La cuenta del usuario " + user.getNombre() + " "
									+ user.getApellidos() + " ha sido activada con éxito.";
							eventPublisher.publishEvent(new UserAccountActivatedEvent(token.getUser(), url));
						}
					} catch (Exception e) {
						resultadoValidacion = "Ocurrió un error durante la activación: " + e.getMessage();
					}
				}
			}
		}

		// Añadir el resultado al modelo para la vista
		modelAndView.addObject("resultadoValidacion", resultadoValidacion);
		return modelAndView;
	}

	/**
	 * Desbloquea la cuenta de usuario utilizando el token proporcionado. Este
	 * método solo es usado por el método verifyNewUserAccount, que es llamado
	 * cuando el admin recibe un email de aviso de nuevo usuario registrado, con
	 * email verificado.
	 *
	 * @param token El token de verificación.
	 * @return Un mensaje indicando el resultado del desbloqueo.
	 */
	public String desbloquearUserAccount(Token token) {
		String resultado = "";
		User user = token.getUser();
		boolean isHabilitada = user.isEnabled();
		boolean isDesbloqueada = user.isNotLocked();

		if (isHabilitada && !isDesbloqueada) {
			try {
				// en caso que el token sea válido, habilitamos la cuenta del usuario y la
				// actualizamos en al BD
				user.setNotLocked(true);
				userRepository.save(user);
				resultado = "desbloqueado";
			} catch (Exception e) {
				resultado = "No ha sido posible desbloquear al usuario: " + e.getMessage();
			}
		}

		return resultado;
	}

	/**
	 * Actualiza el rol de un usuario en el repositorio.
	 *
	 * @param userId  El ID del usuario cuyo rol se va a actualizar.
	 * @param newRole El nuevo rol del usuario.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> updateUserRole(Long userId, Role newRole) throws UserNotFoundException {
		GeneralResponseDto response = new GeneralResponseDto();

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("No se ha encontrado el usuario para cambiar su rol"));
		try {
			if (!user.getRole().equals(newRole)) {
				user.setRole(newRole);
				userRepository.save(user);
				response.setStatus(HttpStatus.OK);
				response.setMessage("El rol del usuario se ha modificado con éxito");
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("El nuevo rol es igual al rol actual. No se han realizado cambios.");
			}
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Ha ocurrido un error y no se ha podido guardar el usuario: " + e.getMessage());
		}
		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Bloquea la cuenta de un usuario en el repositorio. Método usado por el
	 * administrador para bloquear usuarios.
	 *
	 * @param userId El ID del usuario cuya cuenta se va a deshabilitar.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> disableUserAccount(Long userId) throws UserNotFoundException {
		GeneralResponseDto response = new GeneralResponseDto();

		// Busca el usuario en la base de datos
		User user = userRepository.findById(userId).orElseThrow(
				() -> new UserNotFoundException("No se ha encontrado el usuario solicitado para bloquear su cuenta"));

		// Maneja el bloqueo del usuario
		try {
			if (user.isNotLocked()) {
				user.setNotLocked(false);
				userRepository.save(user);
				response.setStatus(HttpStatus.OK);
				response.setMessage("La cuenta del usuario ha sido bloqueada con éxito");
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("La cuenta del usuario ya está bloqueada.");
			}
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Ha ocurrido un error y no se ha podido bloquear el usuario: " + e.getMessage());
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Desbloquea la cuenta de un usuario en el repositorio. Método usado por el
	 * admin para desbloquear usuarios.
	 *
	 * @param userId El ID del usuario cuya cuenta se va a habilitar.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> enableUserAccount(Long userId) throws UserNotFoundException {
		GeneralResponseDto response = new GeneralResponseDto();

		// Busca el usuario en la base de datos
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
				"No se ha encontrado el usuario solicitado para desbloquear su cuenta"));

		// Maneja el bloqueo del usuario
		try {
			if (!user.isNotLocked()) {
				user.setNotLocked(true);
				userRepository.save(user);
				response.setStatus(HttpStatus.OK);
				response.setMessage("La cuenta del usuario ha sido desbloqueada con éxito");
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("La cuenta del usuario ya está desbloqueada.");
			}
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Ha ocurrido un error y no se ha podido desbloquear el usuario: " + e.getMessage());
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

}
