package com.bailaconsarabackend.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bailaconsarabackend.dto.AuthResponseDto;
import com.bailaconsarabackend.dto.AuthenticationRequestDto;
import com.bailaconsarabackend.dto.RegisterRequestDto;
import com.bailaconsarabackend.dto.UserDto;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.exception.UserAlreadyExistsException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.service.AdminService;
import com.bailaconsarabackend.service.AuthService;
import com.bailaconsarabackend.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controlador REST que maneja la autenticación y gestión de usuarios en el
 * sistema. Proporciona endpoints para el registro de usuarios, autenticación,
 * verificación de email, y gestión del perfil de usuario.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final AdminService adminService;
	private final UserService userService;

	/**
	 * Constructor de AuthController.
	 *
	 * @param authService  Servicio de autenticación.
	 * @param adminService Servicio de administración.
	 * @param userService  Servicio de usuario.
	 */
	public AuthController(AuthService authService, AdminService adminService, UserService userService) {
		this.authService = authService;
		this.adminService = adminService;
		this.userService = userService;
	}

	/**
	 * Registra un nuevo usuario en el sistema. Este método crea un nuevo usuario
	 * con los detalles proporcionados en el objeto RegisterRequest, codifica la
	 * contraseña utilizando un codificador de contraseñas, guarda el usuario en la
	 * base de datos, y genera un token JWT para el usuario recién registrado.
	 *
	 * @param request,        El objeto RegisterRequestDto que contiene los detalles
	 *                        del usuario a registrar.
	 * @param servletRequest, La solicitud HTTP del servlet que contiene la URL de
	 *                        la aplicación.
	 * @return Un ResponseEntity que contiene un objeto AuthResponseDto con el token
	 *         JWT generado para el usuario.
	 * @throws UserAlreadyExistsException
	 */
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request,
			final HttpServletRequest servletRequest) throws UserAlreadyExistsException {
		return authService.register(request, servletRequest);
	}

	/**
	 * Registra un nuevo administrador.
	 *
	 * @param requestla      solicitud de registro
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado del registro y su estado
	 *         correspondiente
	 */
//	@PostMapping("/registerNewAdmin")
//	public ResponseEntity<AuthResponseDto> registerNewAdmin(@Valid @RequestBody RegisterRequestDto request, final HttpServletRequest servletRequest){
//		return authService.registerAdmin(request, servletRequest);
//	}

	/**
	 * Autentica a un usuario existente en el sistema. Este método autentica al
	 * usuario utilizando el email y la contraseña proporcionados en el objeto
	 * AuthenticationRequest. Si la autenticación es exitosa, genera un token JWT
	 * para el usuario autenticado y lo devuelve en un objeto AuthResponse.
	 *
	 * @param request el objeto AuthenticationRequest que contiene el email y la
	 *                contraseña del usuario a autenticar.
	 * @return un ResponseEntity que contiene un objeto AuthResponseDto con el token
	 *         JWT generado para el usuario autenticado.
	 * @throws UserNotFoundException si no se encuentra el usuario con el email
	 *                               proporcionado.
	 * @throws NotFoundException
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthenticationRequestDto request)
			throws UserNotFoundException, NotFoundException {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	/**
	 * Verifica la autenticidad de un token de verificación de email y activa la
	 * cuenta de usuario correspondiente.
	 *
	 * @param token          el token de verificación de email.
	 * @param servletRequest el objeto HttpServletRequest que contiene la solicitud
	 *                       HTTP.
	 * @return un mensaje indicando el resultado de la verificación.
	 */
	@GetMapping("/verifyEmail")
	public ModelAndView verifyEmail(@RequestParam("token") String token, final HttpServletRequest servletRequest) {
		return authService.verifyEmail(token, servletRequest);
	}

	/**
	 * Reenvía un nuevo token de verificación de email para la activación de la
	 * cuenta del usuario.
	 *
	 * @param oldToken       el token de verificación de email anterior.
	 * @param servletRequest el objeto HttpServletRequest que contiene la solicitud
	 *                       HTTP.
	 * @return un mensaje indicando que se ha enviado un nuevo token de verificación
	 *         de email.
	 * @throws UnsupportedEncodingException si se produce un error al codificar el
	 *                                      mensaje de email.
	 * @throws MessagingException           si se produce un error al enviar el
	 *                                      mensaje de email.
	 */
	@GetMapping("/resdend-verifyEmail")
	public ModelAndView resendVerificationToken(@RequestParam("token") String oldToken,
			final HttpServletRequest servletRequest) throws UnsupportedEncodingException, MessagingException {
		return authService.resendVerificationEmail(oldToken, servletRequest);
	}

	/**
	 * Verifica la autenticidad de un token de verificación de nueva cuenta de
	 * usuario y debloquea la cuenta correspondiente. Es usado una vez que el
	 * usuario a verificado su email, el administrador recibe un email para aprobar
	 * la cuenta del usuario (la desbloquea)
	 *
	 * @param token          el token de verificación de nueva cuenta de usuario.
	 * @param servletRequest el objeto HttpServletRequest que contiene la solicitud
	 *                       HTTP.
	 * @return un mensaje indicando el resultado de la verificación.
	 * @throws TokenNotFoundException
	 */
	@GetMapping("/verifyNewUserAccount")
	public ModelAndView verifyNewUserAccount(@RequestParam("token") String token,
			final HttpServletRequest servletRequest) throws TokenNotFoundException {
		return adminService.verifyNewUserAccount(token, servletRequest);
	}

	/**
	 * Obtiene el perfil de un usuario mediante un token.
	 *
	 * @param token el token del usuario del que se desea obtener el perfil
	 * @return ResponseEntity que contiene un UserDto si se encuentra el usuario
	 *         asociado al token
	 * @throws UserNotFoundException  si el usuario no se encuentra en la base de
	 *                                datos
	 * @throws TokenNotFoundException si el token no se encuentra en la base de
	 *                                datos
	 */
	@GetMapping("/userProfile")
	public ResponseEntity<UserDto> getUserById(@RequestParam("token") String token)
			throws UserNotFoundException, TokenNotFoundException {
		return userService.findByToken(token);
	}

	/**
	 * Endpoint REST para actualizar la información de un usuario.
	 *
	 * @param request El objeto UserDto que contiene la información actualizada del
	 *                usuario.
	 * @param userId  El ID del usuario que se va a actualizar.
	 * @return ResponseEntity<UserDto> ResponseEntity que contiene el objeto UserDto
	 *         actualizado en caso de éxito.
	 * @throws UserNotFoundException Si el usuario con el ID proporcionado no se
	 *                               encuentra en la base de datos.
	 */
	@PutMapping("/user/{id}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto request, @PathVariable("id") Long userId)
			throws UserNotFoundException {
		return userService.updateUser(request, userId);
	}

}
