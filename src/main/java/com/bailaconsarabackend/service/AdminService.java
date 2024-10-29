package com.bailaconsarabackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Role;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interfaz que define los métodos administrativos para la gestión de usuarios
 * en el sistema. Ofrece operaciones para la actualización de roles, activación
 * y desactivación de cuentas, y verificación de nuevas cuentas de usuario.
 */
public interface AdminService {

	/**
	 * Actualiza el rol de un usuario dado su ID.
	 *
	 * @param userId  El ID del usuario cuyo rol se actualizará.
	 * @param newRole El nuevo rol que se asignará al usuario.
	 * @return ResponseEntity con una respuesta que indica el resultado de la
	 *         actualización del rol.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               especificado.
	 */
	ResponseEntity<GeneralResponseDto> updateUserRole(Long userId, Role newRole) throws UserNotFoundException;

	/**
	 * Desactiva la cuenta de usuario asociada al ID especificado.
	 *
	 * @param userId El ID del usuario cuya cuenta se desactivará.
	 * @return ResponseEntity con una respuesta que indica el resultado de la
	 *         desactivación de la cuenta.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               especificado.
	 */
	ResponseEntity<GeneralResponseDto> disableUserAccount(Long userId) throws UserNotFoundException;

	/**
	 * Verifica la nueva cuenta de usuario utilizando el token proporcionado.
	 *
	 * @param token         El token de verificación de la cuenta de usuario.
	 * @param serverRequest La solicitud HTTP del servidor.
	 * @return El mensaje de confirmación de la verificación de la cuenta de
	 *         usuario.
	 * @throws TokenNotFoundException
	 */
	ModelAndView verifyNewUserAccount(String token, HttpServletRequest serverRequest) throws TokenNotFoundException;

	/**
	 * Activa la cuenta de usuario asociada al ID especificado.
	 *
	 * @param userId El ID del usuario cuya cuenta se activará.
	 * @return ResponseEntity con una respuesta que indica el resultado de la
	 *         activación de la cuenta.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               especificado.
	 */
	ResponseEntity<GeneralResponseDto> enableUserAccount(Long userId) throws UserNotFoundException;
}
