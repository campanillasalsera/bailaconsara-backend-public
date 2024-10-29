package com.bailaconsarabackend.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.User_Taller;

import jakarta.mail.MessagingException;

/**
 * Interfaz que define los servicios de envío de correos electrónicos para
 * diversas notificaciones relacionadas con el sistema. Ofrece métodos para
 * enviar correos de verificación, notificaciones de cuenta, y otras
 * actualizaciones a los usuarios y administradores.
 */
public interface EmailService {

	/**
	 * Envía una notificación al administrador sobre el registro de un nuevo
	 * usuario.
	 *
	 * @param userName  El nombre de usuario registrado.
	 * @param url       El URL para activar la cuenta del usuario.
	 * @param adminName El nombre del administrador al que se enviará la
	 *                  notificación.
	 * @throws MessagingException           Si ocurre un error durante el envío del
	 *                                      correo electrónico.
	 * @throws UnsupportedEncodingException Si ocurre un error de codificación de
	 *                                      caracteres.
	 */
	void sendAdminNotification(String userNamem, String url, String adminName)
			throws MessagingException, UnsupportedEncodingException;

	/**
	 * Envía un correo electrónico de verificación al usuario registrado.
	 *
	 * @param user El usuario al que se enviará el correo electrónico de
	 *             verificación.
	 * @param url  El URL para verificar el correo electrónico y activar la cuenta
	 *             del usuario.
	 * @throws UnsupportedEncodingException Si ocurre un error de codificación de
	 *                                      caracteres.
	 * @throws MessagingException           Si ocurre un error durante el envío del
	 *                                      correo electrónico.
	 */
	void sendVerificationEmail(User user, String url) throws UnsupportedEncodingException, MessagingException;

	/**
	 * Envía una notificación al usuario cuando su cuenta ha sido activada.
	 *
	 * @param user El usuario al que se enviará la notificación.
	 * @param url  El URL para iniciar sesión después de que la cuenta haya sido
	 *             activada.
	 * @throws UnsupportedEncodingException Si ocurre un error de codificación de
	 *                                      caracteres.
	 * @throws MessagingException           Si ocurre un error durante el envío del
	 *                                      correo electrónico.
	 */
	void sendUserAccountActivatedNotification(User user, String url)
			throws UnsupportedEncodingException, MessagingException;

	/**
	 * Envía un correo electrónico para verificar el cambio de contraseña.
	 *
	 * @param nombre El nombre del usuario que solicitó cambiar la contraseña.
	 * @param email  El correo electrónico del usuario.
	 * @param otp    El código OTP (One-Time Password) para restablecer la
	 *               contraseña.
	 * @param url    La URL a la que el usuario debe acceder para completar el
	 *               proceso de recuperación.
	 * @throws UnsupportedEncodingException Si ocurre un error de codificación de
	 *                                      caracteres.
	 * @throws MessagingException           Si ocurre un error durante el envío del
	 *                                      correo electrónico.
	 */
	void sendVerifyEmailForgotPassword(String nombre, String email, int otp)
			throws UnsupportedEncodingException, MessagingException;

	/**
	 * Envía una notificación de nueva pareja asignada.
	 *
	 * @param userTaller el objeto User_Taller asociado
	 * @param partner    el usuario asociado a la nueva pareja
	 * @param url        la URL para la notificación
	 * @throws UnsupportedEncodingException si ocurre un error de codificación no
	 *                                      compatible
	 * @throws MessagingException           si ocurre un error relacionado con el
	 *                                      envío del mensaje
	 */
	void sendNewPartnerNotification(User_Taller userTaller, User partner, String url)
			throws UnsupportedEncodingException, MessagingException;

	/**
	 * Envía una notificación de ausencia de pareja asignada.
	 *
	 * @param userTaller    el objeto User_Taller asociado
	 * @param partner       el usuario asociado a la pareja ausente
	 * @param urlSignOut    la URL para cerrar sesión
	 * @param urlAddPartner la URL para añadir una pareja
	 * @throws UnsupportedEncodingException si ocurre un error de codificación no
	 *                                      compatible
	 * @throws MessagingException           si ocurre un error relacionado con el
	 *                                      envío del mensaje
	 */
	void sendNoPartnerNotification(User_Taller userTaller, User partner, String urlLogIn)
			throws UnsupportedEncodingException, MessagingException;

	/**
	 * Envía una notificación de pareja asignada.
	 *
	 * @param userTaller   el objeto User_Taller asociado
	 * @param nombrePareja el nombre de la pareja
	 * @param subject      el asunto del mensaje
	 * @param url          la URL para la notificación
	 * @throws UnsupportedEncodingException si ocurre un error de codificación no
	 *                                      compatible
	 * @throws MessagingException           si ocurre un error relacionado con el
	 *                                      envío del mensaje
	 */
	void sendPartnerNotification(User_Taller userTaller, String nombrePareja, String subject, String url)
			throws UnsupportedEncodingException, MessagingException;

	/**
	 * Envía una notificación de evento actualizado.
	 *
	 * @param tallerNombre   el nombre del taller asociado al evento actualizado
	 * @param userTaller     el objeto User_Taller asociado
	 * @param modificaciones la lista de modificaciones realizadas
	 * @throws UnsupportedEncodingException si ocurre un error de codificación no
	 *                                      compatible
	 * @throws MessagingException           si ocurre un error relacionado con el
	 *                                      envío del mensaje
	 */
	void sendUpdatedEventNotification(String tallerNombre, User_Taller userTaller, List<String> modificaciones)
			throws UnsupportedEncodingException, MessagingException;
}
