package com.bailaconsarabackend.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bailaconsarabackend.dto.AuthResponseDto;
import com.bailaconsarabackend.dto.AuthenticationRequestDto;
import com.bailaconsarabackend.dto.ChangePasswordDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.RegisterRequestDto;
import com.bailaconsarabackend.event.EmailVerifiedEvent;
import com.bailaconsarabackend.event.VerificarEmailRegistroEvent;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.exception.UserAlreadyExistsException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.ForgotPassword;
import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.Token;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.repository.ForgotPasswordRepository;
import com.bailaconsarabackend.repository.TokenRepository;
import com.bailaconsarabackend.repository.UserRepository;
import com.bailaconsarabackend.service.AuthService;
import com.bailaconsarabackend.service.EmailService;
import com.bailaconsarabackend.service.JwtService;
import com.bailaconsarabackend.service.UserService;
import com.bailaconsarabackend.util.ApplicationUrlUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Implementación del servicio de autenticación que define los servicios de
 * autenticación y gestión de cuentas de usuario en el sistema. Ofrece métodos
 * para registrar, autenticar, verificar, y gestionar la recuperación de
 * contraseñas de usuarios.
 */
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final UserService userService;
	private final EmailService emailService;
	private final ForgotPasswordRepository forgotPasswordRepository;
	private final ApplicationUrlUtil applicationUrlUtil;

	/**
	 * Constructor de AuthServiceImpl.
	 *
	 * @param userRepository           Repositorio de usuarios.
	 * @param passwordEncoder          Codificador de contraseñas.
	 * @param jwtService               Servicio JWT.
	 * @param authenticationManager    Administrador de autenticación.
	 * @param tokenRepository          Repositorio de tokens.
	 * @param eventPublisher           Publicador de eventos de aplicación.
	 * @param userService              Servicio de usuarios.
	 * @param emailService             Servicio de email.
	 * @param forgotPasswordRepository Repositorio de contraseñas olvidadas.
	 * @param applicationUrlUtil       Utilidad para construir URLs de la
	 *                                 aplicación, utilizada para generar URLs
	 *                                 dinámicas basadas en la configuración del
	 *                                 servidor.
	 */
	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager, TokenRepository tokenRepository,
			ApplicationEventPublisher eventPublisher, UserService userService, EmailService emailService,
			ForgotPasswordRepository forgotPasswordRepository, ApplicationUrlUtil applicationUrlUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
		this.eventPublisher = eventPublisher;
		this.userService = userService;
		this.emailService = emailService;
		this.forgotPasswordRepository = forgotPasswordRepository;
		this.applicationUrlUtil = applicationUrlUtil;
	}

	/**
	 * Registra un nuevo usuario en el sistema. Este método crea un nuevo usuario
	 * con los detalles proporcionados en el objeto RegisterRequest, codifica la
	 * contraseña utilizando un codificador de contraseñas, guarda el usuario en la
	 * base de datos, y genera un token JWT para el usuario recién registrado.
	 *
	 * @param request       El objeto RegisterRequestDto que contiene los detalles
	 *                      del usuario a registrar.
	 * @param serverRequest La solicitud HTTP del servidor que contiene la URL de la
	 *                      aplicación.
	 * @return Un ResponseEntity que contiene un objeto AuthResponseDto con el token
	 *         JWT generado para el usuario.
	 * @throws UserAlreadyExistsException
	 */
	@Override
	public ResponseEntity<AuthResponseDto> register(RegisterRequestDto request, HttpServletRequest serverRequest)
			throws UserAlreadyExistsException {

		ResponseEntity<AuthResponseDto> responseEntity = null;

		// Verificación si el usuario ya existe
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("Ya existe un usuario con este email " + request.getEmail());
		} else {
			User user = new User();
			user.setNombre(request.getNombre());
			user.setApellidos(request.getApellidos());
			user.setFechanacimiento(request.getFechanacimiento());
			user.setTelefono(request.getTelefono());
			user.setEmail(request.getEmail());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setBailerol(request.getBailerol());
			user.setRole(Role.USER);

			try {

				// Guardado del nuevo usuario en la base de datos
				userRepository.save(user);
				// Generación del token y publicación del evento de verificación
				String jwtToken = jwtService.generateToken(user);

				saveUserToken(user, jwtToken);

				eventPublisher.publishEvent(
						new VerificarEmailRegistroEvent(user, applicationUrlUtil.applicationUrl(serverRequest)));

				AuthResponseDto authResponse = new AuthResponseDto(jwtToken,
						"Solicitud de registro creada con éxito. Revisa tu email para confirmar tu registro. Revisa tu bandeja de spam.");
				responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CREATED);
			} catch (Exception e) {
				// Manejo de cualquier excepción que pueda ocurrir al intentar guardar el
				// usuario
				AuthResponseDto authResponse = new AuthResponseDto(null,
						"Ocurrió un error al intentar registrar el usuario: " + e.getMessage());
				responseEntity = new ResponseEntity<>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return responseEntity;
	}

	/**
	 * Genera un nuevo token de autenticación basado en un token antiguo.
	 *
	 * @param oldToken el token antiguo.
	 * @return el nuevo token generado.
	 */
	public Token generateNewToken(String oldToken) {
		Optional<Token> theToken = tokenRepository.findByToken(oldToken);
		Token token = theToken.get();
		User user = token.getUser();
		token.setToken(jwtService.generateToken(user));
		return tokenRepository.save(token);
	}

	/**
	 * Guarda el token JWT generado para el usuario en la base de datos.
	 *
	 * @param user     el usuario para el que se genera el token JWT.
	 * @param jwtToken el token JWT generado.
	 */
	public void saveUserToken(User user, String jwtToken) {
		Token token = new Token();
		token.setToken(jwtToken);
		token.setLoggedout(false);
		token.setUser(user);
		tokenRepository.save(token);
	}

	/**
	 * Autentica a un usuario basado en el request de autenticación.
	 *
	 * @param request el objeto de solicitud de autenticación que contiene el email
	 *                y la contraseña del usuario
	 * @return una respuesta de autenticación que contiene el token JWT y un mensaje
	 *         de éxito
	 * @throws UserNotFoundException   si no se encuentra un usuario con el email
	 *                                 proporcionado
	 * @throws NotFoundException
	 * @throws BadCredentialsException si las credenciales proporcionadas son
	 *                                 incorrectas
	 * @throws DisabledException       si la cuenta del usuario está deshabilitada
	 * @throws LockedException         si la cuenta del usuario está bloqueada
	 */
	@Override
	public AuthResponseDto authenticate(AuthenticationRequestDto request)
			throws UserNotFoundException, NotFoundException {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
					() -> new UserNotFoundException("No se ha encontrado el email con el que intenta iniciar sesión"));
			deleteTokenByUser(user);
			String jwtToken = jwtService.generateToken(user);
			saveUserToken(user, jwtToken);
			return new AuthResponseDto(jwtToken, "Inicio de sesión completada con éxito.");
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Las credenciales proporcionadas son incorrectas.");
		} catch (DisabledException e) {
			throw new DisabledException("La cuenta del usuario está deshabilitada.");
		} catch (LockedException e) {
			throw new LockedException(
					"La cuenta del usuario está bloqueada. Revisa tu email, incluida la bandeja de spam.");
		}

	}

	/**
	 * Elimina el token JWT anterior del usuario de la base de datos.
	 *
	 * @param user el usuario del que se eliminará el token JWT.
	 * @throws NotFoundException
	 */
	private void deleteTokenByUser(User user) throws TokenNotFoundException {
		Token oldToken = tokenRepository.findTokenByUser(user.getId())
				.orElseThrow(() -> new TokenNotFoundException("No se ha encontrado el token para este usuario."));
		tokenRepository.delete(oldToken);
	}

	/**
	 * Verifica la autenticidad de un token de verificación de email y activa la
	 * cuenta de usuario correspondiente.
	 *
	 * @param token         el token de verificación de email.
	 * @param serverRequest el objeto HttpServletRequest que contiene la solicitud
	 *                      HTTP.
	 * @return un mensaje indicando el resultado de la verificación.
	 */
	@Override
	public ModelAndView verifyEmail(String stringToken, HttpServletRequest serverRequest) {
		ModelAndView modelAndView = new ModelAndView("verifyEmailResult");
		// Url por si el token ha expirado
		String url = this.applicationUrlUtil.applicationUrl(serverRequest) + "/auth/resdend-verifyEmail?token="
				+ stringToken;

		String resultadoValidacion = "";
		Optional<Token> tokenOptional;
		try {
			// En el caso de no estar presente, se lanza un NotSuchElementException en el
			// último catch
			tokenOptional = tokenRepository.findByToken(stringToken);
			Token theToken = tokenOptional.get();
			boolean isHabilitada = theToken.getUser().isEnabled();
			if (isHabilitada) {
				resultadoValidacion = "Esta cuenta ya ha sido verificada.";
			} else {
				try {
					userService.validateToken(stringToken);
					if (theToken.getUser().getRole().name().equals("ADMIN")) {
						resultadoValidacion = "Tu cuenta ha sido verificada con éxito.";
					} else {
						resultadoValidacion = "Tu cuenta ha sido verificada con éxito. Tu solicitud de registro está a la espera de ser autorizada."
								+ " Recibirás un email cuando tu cuenta haya sido aprobada.";
						eventPublisher.publishEvent(new EmailVerifiedEvent(theToken.getUser(),
								applicationUrlUtil.applicationUrl(serverRequest)));
					}
				} catch (ExpiredJwtException ex) {
					resultadoValidacion = "El enlace ha expirado. Por favor, solicita uno nuevo <a href=\"" + url
							+ "\"> aquí. </a>";
				} catch (Exception ex) {
					resultadoValidacion = "Verificación inválida";
				}
			}
			// Uso NoSuchElementoException en vez de TokenNotFoundException para que no
			// devuelva el mensaje en un json
		} catch (NoSuchElementException e) {
			resultadoValidacion = "Estás usando un enlace antiguo que ya no es válido."
					+ " Comprueba si has recibido un email posterior a este"
					+ ", o prueba a iniciar sesión en nuestra plataforma.";
		}

		// Añadir el resultado al modelo para la vista
		modelAndView.addObject("resultadoValidacion", resultadoValidacion);
		return modelAndView;
	}

	/**
	 * Reenvía un email de verificación con un nuevo token de verificación para un
	 * usuario.
	 *
	 * @param oldToken       el token antiguo.
	 * @param servletRequest el objeto HttpServletRequest que contiene la solicitud
	 *                       HTTP.
	 * @return un mensaje indicando que se ha enviado un nuevo token de verificación
	 *         de email.
	 * @throws MessagingException           si se produce un error al enviar el
	 *                                      mensaje de email.
	 * @throws UnsupportedEncodingException si se produce un error al codificar el
	 *                                      mensaje de email.
	 */
	@Override
	public ModelAndView resendVerificationEmail(String oldToken, HttpServletRequest servletRequest)
			throws MessagingException, UnsupportedEncodingException {
		ModelAndView modelAndView = new ModelAndView("verifyEmailResult");
		String result = "Ha ocurrido un error al reenviar email de verificación";
		try {
			Token token = this.generateNewToken(oldToken);
			User user = token.getUser();
			String url = this.applicationUrlUtil.applicationUrl(servletRequest) + "/auth/verifyEmail?token="
					+ token.getToken();
			emailService.sendVerificationEmail(user, url);
			result = "Se ha enviado un nuevo enlace de verificación a tu email, por favor, revise su email para actuvar tu cuenta.";
		} catch (UnsupportedEncodingException e) {
			result = "Error al codificar el correo electrónico.";

		} catch (MessagingException e) {
			result = "Error al enviar el correo electrónico de verificación.";
		} catch (Exception e) {
			result = "Ocurrió un error inesperado: " + e.getMessage();
		}

		// Añadir el resultado al modelo para la vista
		modelAndView.addObject("resultadoValidacion", result);
		return modelAndView;
	}

	/**
	 * Envía un email de verificación para restablecer la contraseña olvidada del
	 * usuario.
	 *
	 * @param email          El email del usuario que solicita restablecer su
	 *                       contraseña.
	 * @param servletRequest La solicitud HTTP que permite acceder a detalles del
	 *                       servidor, como encabezados.
	 * @return una ResponseEntity con un objeto GeneralResponseDto que indica que se
	 *         ha enviado un email para la verificación.
	 * @throws UserNotFoundException        si no se encuentra al usuario con el
	 *                                      email especificado.
	 * @throws UnsupportedEncodingException si ocurre un error de codificación no
	 *                                      soportado al enviar el email.
	 * @throws MessagingException           si ocurre un error al enviar el email.
	 * @throws NotFoundException
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> verifyEmailForgotPassword(String email, HttpServletRequest servletRequest)
			throws UserNotFoundException, UnsupportedEncodingException, MessagingException, NotFoundException {
		GeneralResponseDto response = new GeneralResponseDto();

		try {
			User user = userRepository.findByEmail(email).orElseThrow(
					() -> new UserNotFoundException("No se ha encontrado el email con el que intenta iniciar sesión"));

			// borra el token antiguo para generar uno nuevo
			this.deleteTokenByUser(user);
			String jwtToken = jwtService.generateToken(user);
			this.saveUserToken(user, jwtToken);

			// Borra la contraseña temporal antigua, si la hubiera
			Optional<ForgotPassword> optionalForgotPassword = forgotPasswordRepository.findByUser(user);
			if (optionalForgotPassword.isPresent()) {
				ForgotPassword forgotPassword = optionalForgotPassword.get();
				forgotPasswordRepository.deleteById(forgotPassword.getId());
			}

			// Genera una nueva contraseña temporal
			int otp = this.otpGenerator();
			ForgotPassword forgotPassword = new ForgotPassword();
			forgotPassword.setOtp(otp);
			forgotPassword.setExpirationTime(LocalDateTime.now().plusMinutes(5));
			forgotPassword.setUser(user);
			forgotPasswordRepository.save(forgotPassword);

			// envía la nueva contraseña temporal al usario por email
			emailService.sendVerifyEmailForgotPassword(user.getNombre(), email, otp);

			response.setStatus(HttpStatus.OK);
			response.setMessage("Te hemos enviado un email con una contraseña de un solo uso.");
		} catch (UserNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage(e.getMessage());
		} catch (TokenNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Error al codificar el correo electrónico.");
		} catch (MessagingException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Error al enviar el email. Por favor, inténtelo de nuevo.");
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Ocurrió un error inesperado. Por favor, inténtelo de nuevo.");
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Genera un número de contraseña de un solo uso (OTP) de 6 dígitos.
	 *
	 * @return el número de OTP generado.
	 */
	private int otpGenerator() {
		Random random = new Random();

		return random.nextInt(100_000, 999_999);
	}

	/**
	 * Verifica la contraseña de un solo uso (OTP) proporcionada por el usuario.
	 *
	 * @param otp   la contraseña de un solo uso proporcionada por el usuario.
	 * @param email el email del usuario para el cual se está verificando la OTP.
	 * @return una ResponseEntity con un objeto GeneralResponseDto que indica el
	 *         resultado de la verificación.
	 * @throws UserNotFoundException si no se encuentra al usuario con el email
	 *                               especificado.
	 * @throws NotFoundException
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> verifyOtp(Integer otp, String email)
			throws UserNotFoundException, NotFoundException {
		GeneralResponseDto response = new GeneralResponseDto();
		try {
			User user = userRepository.findByEmail(email).orElseThrow(
					() -> new UserNotFoundException("No se ha encontrado al usuario asociado a este email."));

			ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user)
					.orElseThrow(() -> new NotFoundException());

			// Si la contraseña OTP ha expirdo la borramos y devuelve un HttpResponse
			if (forgotPassword.getExpirationTime().isBefore(LocalDateTime.now())) {
				response.setMessage(
						"¡La contraseña de un solo uso ha expirado! Reinicia el proceso, para recibir una nueva.");
				response.setStatus(HttpStatus.EXPECTATION_FAILED);
			} else {
				forgotPassword.setVerified(true);
				forgotPasswordRepository.save(forgotPassword);
				response.setMessage("¡Contraseña de un solo uso verificada!");
				response.setStatus(HttpStatus.OK);
			}
		} catch (UserNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage(e.getMessage());
		} catch (NotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage("Contraseña de un solo uso inválida");
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Ocurrió un error inesperado. Por favor, inténtelo de nuevo.");
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Cambia la contraseña de un usuario después de verificar la contraseña de un
	 * solo uso (OTP).
	 *
	 * @param changePasswordDto el objeto ChangePasswordDto que contiene la nueva
	 *                          contraseña y su repetición.
	 * @param email             el email del usuario cuya contraseña se va a
	 *                          cambiar.
	 * @return una ResponseEntity con un objeto GeneralResponseDto que contiene el
	 *         resultado del cambio de contraseña.
	 * @throws UserNotFoundException si no se encuentra al usuario con el email
	 *                               especificado.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> changePassword(ChangePasswordDto changePasswordDto, String email)
			throws UserNotFoundException {

		GeneralResponseDto response = new GeneralResponseDto();
		ResponseEntity<GeneralResponseDto> responseEntity;

		String password = changePasswordDto.getPassword();
		String repeatPassword = changePasswordDto.getRepeatPassword();

		try {
			User user = userRepository.findByEmail(email).orElseThrow(
					() -> new UserNotFoundException("No se ha encontrado el usuario asociando a este email."));

			ForgotPassword forgotPassword = forgotPasswordRepository.findByUser(user)
					.orElseThrow(() -> new NotFoundException());

			if (!forgotPassword.isVerified()) {
				response.setStatus(HttpStatus.EXPECTATION_FAILED);
				response.setMessage("¡La contraseña OTP no ha sido verificada!");
			} else {

				if (!password.equals(repeatPassword)) {
					response.setMessage("¡Las contraseñas no coinciden, introduce contraseñas otra vez!");
					response.setStatus(HttpStatus.EXPECTATION_FAILED);
				} else {
					String encodePassword = passwordEncoder.encode(password);
					user.setPassword(encodePassword);
					userRepository.save(user);

					response.setStatus(HttpStatus.OK);
					response.setMessage("¡La contraseña ha sido cambiada!");
				}
			}
		} catch (UserNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage(e.getMessage());
		} catch (NotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage("Contraseña de un solo uso inválida");
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Ocurrió un error inesperado. Por favor, inténtelo de nuevo.");
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

}
