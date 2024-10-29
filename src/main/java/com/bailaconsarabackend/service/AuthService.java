package com.bailaconsarabackend.service;

import java.io.UnsupportedEncodingException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.bailaconsarabackend.dto.AuthResponseDto;
import com.bailaconsarabackend.dto.AuthenticationRequestDto;
import com.bailaconsarabackend.dto.ChangePasswordDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.RegisterRequestDto;
import com.bailaconsarabackend.exception.UserAlreadyExistsException;
import com.bailaconsarabackend.exception.UserNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interfaz que define los servicios de autenticación y gestión de cuentas de
 * usuario en el sistema. Ofrece métodos para registrar, autenticar, verificar,
 * y gestionar la recuperación de contraseñas de usuarios.
 */
public interface AuthService {

	/**
	 * Registra un nuevo usuario con la información proporcionada.
	 *
	 * @param request       La solicitud de registro con los datos del nuevo
	 *                      usuario.
	 * @param serverRequest La solicitud HTTP del servidor.
	 * @return ResponseEntity con una respuesta que contiene los detalles del
	 *         registro.
	 * @throws UserAlreadyExistsException
	 */
	ResponseEntity<AuthResponseDto> register(RegisterRequestDto request, HttpServletRequest serverRequest)
			throws UserAlreadyExistsException;

	/**
	 * Autentica a un usuario utilizando las credenciales proporcionadas.
	 *
	 * @param request La solicitud de autenticación con las credenciales del
	 *                usuario.
	 * @return AuthResponseDto con los detalles de autenticación del usuario.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con las
	 *                               credenciales proporcionadas.
	 * @throws NotFoundException
	 */
	AuthResponseDto authenticate(AuthenticationRequestDto request) throws UserNotFoundException, NotFoundException;

	/**
	 * Verifica el correo electrónico de un usuario utilizando el token
	 * proporcionado.
	 *
	 * @param token         El token de verificación del correo electrónico del
	 *                      usuario.
	 * @param serverRequest La solicitud HTTP del servidor.
	 * @return El mensaje de confirmación de la verificación del correo electrónico.
	 */
	ModelAndView verifyEmail(String token, HttpServletRequest serverRequest);

	/**
	 * Reenvía el correo electrónico de verificación utilizando el token
	 * proporcionado.
	 *
	 * @param oldToken       El token anterior asociado al correo electrónico de
	 *                       verificación.
	 * @param servletRequest La solicitud HTTP del servlet.
	 * @return El mensaje de confirmación del reenvío del correo electrónico de
	 *         verificación.
	 * @throws MessagingException           Si ocurre un error al enviar el correo
	 *                                      electrónico.
	 * @throws UnsupportedEncodingException Si ocurre un error de codificación de
	 *                                      caracteres.
	 */
	ModelAndView resendVerificationEmail(String oldToken, HttpServletRequest servletRequest)
			throws MessagingException, UnsupportedEncodingException;

	/**
	 * Verifica el correo electrónico para restablecer la contraseña del usuario.
	 *
	 * @param email El correo electrónico del usuario cuya contraseña se
	 *              restablecerá.
	 * @return ResponseEntity con una respuesta que indica el resultado de la
	 *         verificación del correo electrónico.
	 * @throws UserNotFoundException        Si no se encuentra ningún usuario con el
	 *                                      correo electrónico especificado.
	 * @throws UnsupportedEncodingException Si ocurre un error de codificación de
	 *                                      caracteres.
	 * @throws MessagingException           Si ocurre un error al enviar el correo
	 *                                      electrónico.
	 * @throws NotFoundException
	 */
	ResponseEntity<GeneralResponseDto> verifyEmailForgotPassword(String email, HttpServletRequest serverRequest)
			throws UserNotFoundException, UnsupportedEncodingException, MessagingException, NotFoundException;

	/**
	 * Verifica el OTP (One-Time Password) enviado al correo electrónico del
	 * usuario.
	 *
	 * @param otp   El OTP enviado al correo electrónico del usuario.
	 * @param email El correo electrónico del usuario.
	 * @return ResponseEntity con una respuesta que indica el resultado de la
	 *         verificación del OTP.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el correo
	 *                               electrónico especificado.
	 * @throws NotFoundException
	 */
	ResponseEntity<GeneralResponseDto> verifyOtp(Integer otp, String email)
			throws UserNotFoundException, NotFoundException;

	/**
	 * Cambia la contraseña del usuario utilizando los datos proporcionados.
	 *
	 * @param changePasswordDto Los datos para cambiar la contraseña del usuario.
	 * @param email             El correo electrónico del usuario.
	 * @return ResponseEntity con una respuesta que indica el resultado del cambio
	 *         de contraseña.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el correo
	 *                               electrónico especificado.
	 */
	ResponseEntity<GeneralResponseDto> changePassword(ChangePasswordDto changePasswordDto, String email)
			throws UserNotFoundException;

}