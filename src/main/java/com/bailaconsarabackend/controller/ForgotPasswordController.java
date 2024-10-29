package com.bailaconsarabackend.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bailaconsarabackend.dto.ChangePasswordDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.service.AuthService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador REST para manejar las operaciones relacionadas con la
 * recuperación de contraseña. Este controlador proporciona endpoints para
 * verificar el email del usuario, verificar la contraseña OTP y cambiar la
 * contraseña del usuario.
 */
@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

	private final AuthService authService;

	/**
	 * Constructor de la clase ForgotPasswordController.
	 * 
	 * @param authService el servicio de autenticación utilizado para manejar las
	 *                    operaciones de recuperación de contraseña.
	 */
	public ForgotPasswordController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * Endpoint para verificar el email del usuario durante el proceso de
	 * recuperación de contraseña.
	 * 
	 * @param email el email del usuario que se debe verificar.
	 * @return una ResponseEntity que contiene un objeto GeneralResponseDto con el
	 *         resultado de la verificación.
	 * @throws UserNotFoundException        si no se encuentra al usuario con el
	 *                                      email especificado.
	 * @throws UnsupportedEncodingException si hay un problema con la codificación
	 *                                      de caracteres durante el proceso de
	 *                                      envío de correo electrónico.
	 * @throws MessagingException           si ocurre un error durante el envío del
	 *                                      correo electrónico.
	 * @throws NotFoundException
	 */
	@PostMapping("/verifyEmail/{email}")
	public ResponseEntity<GeneralResponseDto> verifyEmail(@PathVariable String email,
			final HttpServletRequest servletRequest)
			throws UserNotFoundException, UnsupportedEncodingException, MessagingException, NotFoundException {
		return authService.verifyEmailForgotPassword(email, servletRequest);
	}

	/**
	 * Endpoint para verificar la contraseña OTP (One-Time Password) durante el
	 * proceso de recuperación de contraseña.
	 * 
	 * @param otp   la contraseña de un solo uso que el usuario proporciona para la
	 *              verificación.
	 * @param email el email del usuario para el cual se está verificando la
	 *              contraseña OTP.
	 * @return una ResponseEntity que contiene un objeto GeneralResponseDto con el
	 *         resultado de la verificación.
	 * @throws UserNotFoundException si no se encuentra al usuario con el email
	 *                               especificado.
	 * @throws NotFoundException
	 */
	@PostMapping("/verifyOtp/{otp}/{email}")
	public ResponseEntity<GeneralResponseDto> verifyOtp(@PathVariable Integer otp, @PathVariable String email)
			throws UserNotFoundException, NotFoundException {
		return authService.verifyOtp(otp, email);
	}

	/**
	 * Endpoint para cambiar la contraseña del usuario durante el proceso de
	 * recuperación de contraseña.
	 * 
	 * @param changePasswordDto el objeto que contiene la nueva contraseña y su
	 *                          confirmación.
	 * @param email             el email del usuario para el cual se está cambiando
	 *                          la contraseña.
	 * @return una ResponseEntity que contiene un objeto GeneralResponseDto con el
	 *         resultado del cambio de contraseña.
	 * @throws UserNotFoundException si no se encuentra al usuario con el email
	 *                               especificado.
	 */
	@PostMapping("/changePassword/{email}")
	public ResponseEntity<GeneralResponseDto> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
			@PathVariable String email) throws UserNotFoundException {
		return authService.changePassword(changePasswordDto, email);
	}

}
