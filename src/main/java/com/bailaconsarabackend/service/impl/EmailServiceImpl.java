package com.bailaconsarabackend.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.User_Taller;
import com.bailaconsarabackend.service.EmailService;
import com.bailaconsarabackend.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Implementación del servicio de correo electrónico que define los servicios de
 * envío de correos electrónicos para diversas notificaciones relacionadas con
 * el sistema. Ofrece métodos para enviar correos de verificación,
 * notificaciones de cuenta, y otras actualizaciones a los usuarios y
 * administradores.
 */
@Service
public class EmailServiceImpl implements EmailService {

	private final UserService userService;
	private final JavaMailSender mailSender;

	/**
	 * Constructor de la clase EmailServiceImpl.
	 *
	 * @param userService el servicio de usuario utilizado para buscar información
	 *                    de usuario.
	 * @param mailSender  el servicio de envío de correo electrónico utilizado para
	 *                    enviar correos electrónicos.
	 */
	public EmailServiceImpl(UserService userService, JavaMailSender mailSender) {
		this.userService = userService;
		this.mailSender = mailSender;
	}

	/**
	 * Envía una notificación al administrador sobre el registro de un nuevo
	 * usuario.
	 *
	 * @param userName  el nombre del usuario registrado.
	 * @param url       la URL para activar la cuenta del usuario.
	 * @param adminName el nombre del administrador.
	 * @throws MessagingException           si se produce un error al enviar el
	 *                                      mensaje de correo electrónico.
	 * @throws UnsupportedEncodingException si se produce un error de codificación
	 *                                      no compatible.
	 */
	@Override
	public void sendAdminNotification(String userName, String url, String adminName)
			throws MessagingException, UnsupportedEncodingException {
		String subject = "Nuevo Usuario Registrado";
		String serderName = "Baila con Sara";

		String adminFullName = this.getAdministrador(adminName).getNombre();
		String adminEmail = this.getAdministrador(adminName).getEmail();

		String mailContent = "<p> Hola " + adminFullName + ", </p>"
				+ "<p>Se ha registrado un nuevo usuario en Baila con Sara." + " "
				+ "Por favor, sigue el siguiente enlace para activar su inscripción.</p>" + "<p>Nuevo usuario: "
				+ userName + "</p>" + "<a href=\"" + url + "\">Activa la cuenta del usuario</a>"
				+ "<p> Gracias <br> Servicio de registro de usuarios de Baila con Sara";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(adminEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	/**
	 * Obtiene el administrador por su nombre.
	 *
	 * @param adminName el nombre del administrador.
	 * @return el objeto User que representa al administrador.
	 */
	public User getAdministrador(String adminName) {
		User administrador = new User();
		List<User> admins = userService.findByRole(Role.ADMIN);
		for (User admin : admins) {
			if (admin.getNombre().equalsIgnoreCase(adminName)) {
				administrador = admin;
			}
		}
		return administrador;
	}

	/**
	 * Envía un correo electrónico de verificación al usuario recién registrado.
	 *
	 * @param user el usuario para el que se envía el correo electrónico de
	 *             verificación.
	 * @param url  la URL para verificar el correo electrónico del usuario.
	 * @throws UnsupportedEncodingException si se produce un error de codificación
	 *                                      no compatible.
	 * @throws MessagingException           si se produce un error al enviar el
	 *                                      mensaje de correo electrónico.
	 */
	@Override
	public void sendVerificationEmail(User user, String url) throws UnsupportedEncodingException, MessagingException {
		String userName = user.getNombre();
		String userEmail = user.getEmail();

		String subject = "Verifica tu email";
		String serderName = "Baila con Sara";
		String mailContent = "<p> Hola, " + userName + ", </p>" + "<p>Gracias por registrarte en Baila con Sara." + ""
				+ " Por favor, sigue este enlace para completar tu inscripción.</p>" + "<a href=\"" + url
				+ "\">Verifica tu correo electrónico para activar tu cuenta</a>"
				+ "<p> Gracias <br> Servicio de registro de usuarios de Baila con Sara";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(userEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	/**
	 * Envía una notificación al usuario cuya cuenta ha sido activada por el
	 * administrador.
	 *
	 * @param user el usuario cuya cuenta ha sido activada.
	 * @param url  la URL para iniciar sesión.
	 * @throws UnsupportedEncodingException si se produce un error de codificación
	 *                                      no compatible.
	 * @throws MessagingException           si se produce un error al enviar el
	 *                                      mensaje de correo electrónico.
	 */
	@Override
	public void sendUserAccountActivatedNotification(User user, String urlLogin)
			throws UnsupportedEncodingException, MessagingException {
		String userName = user.getNombre();
		String userEmail = user.getEmail();
		String urlHome = "https://bailaconsara.com";

		String subject = "Tu cuenta ha sido aprobada";
		String serderName = "Baila con Sara";
		String mailContent = "<p> Hola, " + userName + ", </p>" + "<p>Gracias por registrarte en Baila con Sara," + ""
				+ "Tu cuenta ya ha sido activada. Puedes iniciar sesión en el siguiente enlace</p>" + "<a href=\""
				+ urlHome + "\">Ir a inicio de sesión en Baila con Sara</a>"
				+ "<p> Gracias <br> Servicio de registro de usuarios de Baila con Sara";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(userEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	/**
	 * Envía un correo electrónico de verificación para restablecer la contraseña
	 * olvidada del usuario.
	 *
	 * @param nombre el nombre del usuario para el que se envía el correo
	 *               electrónico.
	 * @param email  el correo electrónico del usuario.
	 * @param otp    el OTP generado para restablecer la contraseña.
	 * @param url    La URL a la que el usuario debe acceder para completar el
	 *               proceso de restablecimiento.
	 * @throws UnsupportedEncodingException si se produce un error de codificación
	 *                                      no compatible.
	 * @throws MessagingException           si se produce un error al enviar el
	 *                                      mensaje de correo electrónico.
	 */
	@Override
	public void sendVerifyEmailForgotPassword(String nombre, String email, int otp)
			throws UnsupportedEncodingException, MessagingException {
		String subject = "Cambiar contraseña";
		String serderName = "Baila con Sara";
		String mailContent = "<p> Hola, " + nombre + ", </p>" + "<p>Has solicitado cambiar tu contraseña. "
				+ "Esta es la contraseña de un solo uso para resetear tu contraseña: </p>" + "<strong>" + otp
				+ "</strong>" + "<p>Dispones de 5 minutos para usarla antes de que expire </p>"
				+ "<p> Gracias <br> Servicio de registro de usuarios de Baila con Sara </p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(email);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	/**
	 * Envía una notificación por correo electrónico cuando se asigna una nueva
	 * pareja a un usuario para un evento.
	 * 
	 * @param userTaller El objeto User_Taller que representa al usuario y su
	 *                   participación en el evento.
	 * @param partner    El objeto User que representa a la nueva pareja asignada al
	 *                   usuario.
	 * @param url        El enlace para que el usuario cancele su inscripción en el
	 *                   evento.
	 * @throws UnsupportedEncodingException Si hay un problema con la codificación
	 *                                      del contenido del correo electrónico.
	 * @throws MessagingException           Si ocurre un error al enviar el correo
	 *                                      electrónico.
	 */
	@Override
	public void sendNewPartnerNotification(User_Taller userTaller, User partner, String urlLogin)
			throws UnsupportedEncodingException, MessagingException {

		String tallerName = userTaller.getTaller().getNombre();
		String userName = userTaller.getUser().getNombre();
		String userEmail = userTaller.getUser().getEmail();
		String partnerFullName = partner.getNombre() + " " + partner.getApellidos();
		String lugarTaller = userTaller.getTaller().getLugar();
		String urlHome = "https://bailaconsara.com";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String fechaFormateada = userTaller.getTaller().getFecha().format(formatter);

		String subject = "Pareja para el evento: " + tallerName;
		String serderName = "Baila con Sara";
		String mailContent = "<p> Hola " + userName + ", </p>" + "<p>" + partnerFullName + " será tu pareja"
				+ " para asistir al evento <strong>" + tallerName + "</strong>" + " con fecha " + fechaFormateada
				+ " en " + lugarTaller + "</p>"
				+ " Si no puedes asistir, te rogamos que anules tu solicitud de participación "
				+ "para que pueda asistir otra persona en tu lugar." + "<p>" + "<a href=\"" + urlHome
				+ "\">Accede a tu perfil de usuario para gestionar tu asistencia al evento.</a>"
				+ "<p> Gracias <br> Servicio de eventos de Baila con Sara" + "</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(userEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);

	}

	/**
	 * Envía una notificación por correo electrónico cuando la pareja de un usuario
	 * cancela su asistencia a un evento.
	 * 
	 * @param userTaller    El objeto User_Taller que representa al usuario y su
	 *                      participación en el evento.
	 * @param partner       El objeto User que representa a la pareja que canceló su
	 *                      asistencia.
	 * @param urlSignOut    El enlace para que el usuario cancele su inscripción en
	 *                      el evento.
	 * @param urlAddPartner El enlace para que el usuario añada una nueva pareja al
	 *                      evento.
	 * @throws UnsupportedEncodingException Si hay un problema con la codificación
	 *                                      del contenido del correo electrónico.
	 * @throws MessagingException           Si ocurre un error al enviar el correo
	 *                                      electrónico.
	 */
	@Override
	public void sendNoPartnerNotification(User_Taller userTaller, User partner, String urlLogIn)
			throws UnsupportedEncodingException, MessagingException {
		String userName = userTaller.getUser().getNombre();
		String partnerFullName = partner.getNombre() + " " + partner.getApellidos();
		String tallerName = userTaller.getTaller().getNombre();
		String tallerLugar = userTaller.getTaller().getLugar();
		String userEmail = userTaller.getUser().getEmail();
		String urlHome = "https://bailaconsara.com";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String fechaFormateada = userTaller.getTaller().getFecha().format(formatter);

		String subject = "Tu pareja ha anulado su asistencia al evento: " + userTaller.getTaller().getNombre();
		String serderName = "Baila con Sara";
		String mailContent = "<p> Hola " + userName + ", </p>" + "<p>" + partnerFullName + " ha anulado su asistencia"
				+ " al evento <strong>" + tallerName + "</strong>" + " con fecha " + fechaFormateada + " en "
				+ tallerLugar + "</p>" + "<p>"
				+ " Vuelves a estar en lista de espera hasta conseguir una nueva pareja. Te avisaremos "
				+ "cuando se te asigne una nueva pareja." + "</p>"
				+ "<p>Puedes anular tu inscripción o añadir tu pareja en tu perfil de usuario.</p>" + "<p>"
				+ "<a href=\"" + urlHome + "\">Inicia sesión aquí</a>" + "</p>"
				+ "<p> Gracias <br> Servicio de eventos de Baila con Sara" + "</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(userEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);

	}

	/**
	 * Envía una notificación por correo electrónico cuando se asigna una nueva
	 * pareja a un usuario después de que su pareja anterior haya cancelado su
	 * asistencia a un evento.
	 * 
	 * @param userTaller     El objeto User_Taller que representa al usuario y su
	 *                       participación en el evento.
	 * @param oldPartnerName El nombre de la pareja anterior que canceló su
	 *                       asistencia.
	 * @param newPartnerName El nombre de la nueva pareja asignada al usuario.
	 * @param url            El enlace para que el usuario cancele su inscripción en
	 *                       el evento.
	 * @throws UnsupportedEncodingException Si hay un problema con la codificación
	 *                                      del contenido del correo electrónico.
	 * @throws MessagingException           Si ocurre un error al enviar el correo
	 *                                      electrónico.
	 */
	@Override
	public void sendPartnerNotification(User_Taller userTaller, String oldPartnerName, String newPartnerName,
			String urlLogin) throws UnsupportedEncodingException, MessagingException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String fechaFormateada = userTaller.getTaller().getFecha().format(formatter);
		String serderName = "Baila con Sara";
		String urlHome = "https://bailaconsara.com";

		String subject = "Tu pareja ha anulado su asistencia al evento y te hemos asignado una nueva pareja. ";
		String mailContent = "<p> Hola " + userTaller.getUser().getNombre() + ", </p>" + "<p>" + oldPartnerName
				+ " ha anulado su asistencia" + " al evento <strong>" + userTaller.getTaller().getNombre() + "</strong>"
				+ " con fecha " + fechaFormateada + " en " + userTaller.getTaller().getLugar() + "</p>" + "<p>"
				+ " Te hemos asignado una nueva pareja: " + newPartnerName + "</p>"
				+ "<p>Rogamos que si no puedes asistir anules tu inscripción para que otra persona pueda "
				+ " </p> ocupar tu lugar." + "<p>" + "<a href=\"" + urlHome
				+ "\">Accede a tu perfil de usuario para gestionar tu asistencia al evento.</a>" + "</p>"
				+ "<p> Gracias <br> Servicio de eventos de Baila con Sara" + "</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(userTaller.getUser().getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);

	}

	/**
	 * Envía una notificación por correo electrónico cuando hay modificaciones en
	 * los detalles de un evento al que un usuario está inscrito.
	 * 
	 * @param tallerNombre   El nombre del taller/evento.
	 * @param userTaller     El objeto User_Taller que representa al usuario y su
	 *                       participación en el evento.
	 * @param modificaciones Una lista de cadenas que describen las modificaciones
	 *                       realizadas en el evento.
	 * @throws UnsupportedEncodingException Si hay un problema con la codificación
	 *                                      del contenido del correo electrónico.
	 * @throws MessagingException           Si ocurre un error al enviar el correo
	 *                                      electrónico.
	 */
	@Override
	public void sendUpdatedEventNotification(String tallerNombre, User_Taller userTaller, List<String> modificaciones)
			throws UnsupportedEncodingException, MessagingException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String fechaFormateada = userTaller.getTaller().getFecha().format(formatter);
		String serderName = "Baila con Sara";
		String textoModificaciones = "";
		String datosActualizados = "<p> DATOS ACTUALIZADOS DEL EVENTO: <br> " + "<strong>"
				+ userTaller.getTaller().getNombre() + " con " + userTaller.getTaller().getProfesores()
				+ "</strong><br>" + "Modalidad de baile: " + userTaller.getTaller().getModalidad() + "<br>" + "Fecha: "
				+ fechaFormateada + "<br>" + "Hora: " + userTaller.getTaller().getHora() + "<br>" + "Ubicación: "
				+ userTaller.getTaller().getLugar() + "<br>";
		for (String modificacion : modificaciones) {
			if (modificacion.contains("Se ha modificado la Fecha:")) {
				modificacion = "Hemos movido la Fecha: " + fechaFormateada;
			}
			textoModificaciones += "- " + modificacion + " <br>";

			if (modificacion.contains("Se ha anulado el evento: ")) {
				datosActualizados = "Disculpa las molestias.";
			}
		}
		String subject = "Hay modificaciones en el evento:  " + tallerNombre;
		String mailContent = "<p> Hola " + userTaller.getUser().getNombre() + ", </p>" + "<p> El evento <strong>"
				+ tallerNombre + "</strong> ha sufrido alguna modificación: </p>" + textoModificaciones
				+ datosActualizados + "<p> Gracias <br> Servicio de eventos de Baila con Sara" + "</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("bailaconsarainfo@gmail.com", serderName);
		messageHelper.setTo(userTaller.getUser().getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

}
