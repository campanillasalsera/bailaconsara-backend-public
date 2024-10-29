package com.bailaconsarabackend.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.UserDto;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.Token;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.repository.ForgotPasswordRepository;
import com.bailaconsarabackend.repository.TokenRepository;
import com.bailaconsarabackend.repository.UserRepository;
import com.bailaconsarabackend.service.JwtService;
import com.bailaconsarabackend.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;

/**
 * Implementación del servicio de usuarios que define los servicios para la
 * gestión de usuarios en el sistema. Incluye operaciones como validación de
 * tokens, búsqueda, actualización y eliminación de usuarios, así como la
 * gestión de roles y recuperación de información relacionada con los usuarios.
 */
@Service
public class UserServiceImpl implements UserService {

	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final UserRepository userRepository;
	private final ForgotPasswordRepository forgotPasswordRepository;

	/**
	 * Constructor para la clase UserServiceImpl.
	 *
	 * @param tokenRepository          Repositorio de tokens.
	 * @param jwtService               Servicio JWT.
	 * @param userDetailsService       Detalles del usuario.
	 * @param userRepository           Repositorio de usuarios.
	 * @param forgotPasswordRepository Repositorio de recuperación de contraseña
	 *                                 olvidada.
	 */
	public UserServiceImpl(TokenRepository tokenRepository, JwtService jwtService,
			UserDetailsService userDetailsService, UserRepository userRepository,
			ForgotPasswordRepository forgotPasswordRepository) {
		this.tokenRepository = tokenRepository;
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.userRepository = userRepository;
		this.forgotPasswordRepository = forgotPasswordRepository;
	}

	/**
	 * Valida un token JWT y habilita la cuenta del usuario asociado si el token es
	 * válido.
	 *
	 * @param theToken El token JWT a validar.
	 * @return Un mensaje indicando el resultado de la validación.
	 * @throws ExpiredJwtException    Si el token ha expirado.
	 * @throws TokenNotFoundException
	 */
	@Override
	public String validateToken(String theToken) throws ExpiredJwtException, TokenNotFoundException {
		String mensaje = "";

		Token token = tokenRepository.findByToken(theToken)
				.orElseThrow(() -> new TokenNotFoundException("Token no encontrado"));

		// si no encontramos el token devolvemos un mensaje
		if (token == null) {
			mensaje = "link de verificación invalido";
		} else {
			// Si el token existe cogemos el usuario
			User user = token.getUser();
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getEmail());

			// comprobamos si el token sea válido
			if (!jwtService.validateToken(token.getToken(), userDetails)) {
				throw new ExpiredJwtException(null, null, mensaje);
			} else {
				// en caso que el token sea válido, habilitamos la cuenta del usuario y la
				// actualizamos en al BD
				user.setEnabled(true);
				userRepository.save(user);
				mensaje = "valid";
			}
		}

		return mensaje;
	}

	/**
	 * Encuentra usuarios por su rol.
	 *
	 * @param role El rol de los usuarios a buscar.
	 * @return La lista de usuarios con el rol especificado.
	 */
	@Override
	public List<User> findByRole(Role role) {
		return userRepository.findByRole(role);
	}

	/**
	 * Elimina el token de un usuario.
	 *
	 * @param user El usuario del cual se eliminará el token.
	 * @throws TokenNotFoundException
	 */
	@Override
	public void deleteUserToken(User user) throws TokenNotFoundException {
		Token token = tokenRepository.findTokenByUser(user.getId())
				.orElseThrow(() -> new TokenNotFoundException("No se ha encontrado el token"));
		try {
			tokenRepository.delete(token);
		} catch (Exception e) {
			throw new RuntimeException("Error al intentar eliminar el token del usuario con ID: " + user.getId(), e);
		}
	}

	/**
	 * Busca un usuario por su ID en el repositorio.
	 *
	 * @param id El ID del usuario que se va a buscar.
	 * @return ResponseEntity que contiene el usuario encontrado.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@Override
	public ResponseEntity<User> findById(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
		return ResponseEntity.ok(user);
	}

	/**
	 * Recupera un usuario por su dirección de correo electrónico desde el
	 * repositorio.
	 *
	 * @param email La dirección de correo electrónico del usuario que se va a
	 *              recuperar.
	 * @return El usuario correspondiente a la dirección de correo electrónico
	 *         especificada.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con la
	 *                               dirección de correo electrónico proporcionada.
	 */
	@Override
	public User findByEmail(String email) throws UserNotFoundException {
		Optional<User> user = userRepository.findByEmail(email);
		return user
				.orElseThrow(() -> new UserNotFoundException("No se ha encontrado el usuario con el email " + email));
	}

	/**
	 * Lista todos los usuarios y construye una respuesta básica.
	 *
	 * @return Una respuesta básica que contiene una lista de usuarios y un mensaje
	 *         de estado.
	 */
	@Override
	public List<UserDto> listUsers() {
		List<User> allUsers = userRepository.findAll();

		// Mapeo de lista de usuarios a lista de usuariosDto
		List<UserDto> usersListDto = allUsers.stream()
				.map(user -> new UserDto(user.getId(), user.getNombre(), user.getApellidos(), user.getFechanacimiento(),
						user.getTelefono(), user.getEmail(), user.getBailerol(), user.getRole().toString(),
						user.isEnabled(), user.isNotLocked()))
				.collect(Collectors.toList());

		return usersListDto;
	}

	/**
	 * Actualiza la información de un usuario en la base de datos.
	 *
	 * @param request El objeto UserDto que contiene la información actualizada del
	 *                usuario.
	 * @param userId  El ID del usuario que se va a actualizar.
	 * @return ResponseEntity<UserDto> ResponseEntity que contiene el objeto UserDto
	 *         actualizado en caso de éxito.
	 * @throws UserNotFoundException Si el usuario con el ID proporcionado no se
	 *                               encuentra en la base de datos.
	 */
	@Override
	public ResponseEntity<UserDto> updateUser(UserDto request, Long userId) throws UserNotFoundException {
		ResponseEntity<UserDto> response;
		User user = userRepository.findById(userId).orElseThrow(
				() -> new UserNotFoundException("No se ha encontrado el usuario solicitado para modificar"));

		if (!Objects.equals(user.getNombre(), request.getNombre()) && request.getNombre() != null
				&& !request.getNombre().isBlank()) {
			user.setNombre(request.getNombre());
		}
		if (!Objects.equals(user.getApellidos(), request.getApellidos()) && request.getApellidos() != null
				&& !request.getApellidos().isBlank()) {
			user.setApellidos(request.getApellidos());
		}
		if (!Objects.equals(user.getFechanacimiento(), request.getFechanacimiento())
				&& request.getFechanacimiento() != null) {
			user.setFechanacimiento(request.getFechanacimiento());
		}
		if (!Objects.equals(user.getTelefono(), request.getTelefono()) && request.getTelefono() != null
				&& !request.getTelefono().isBlank()) {
			user.setTelefono(request.getTelefono());
		}
		if (!Objects.equals(user.getBailerol(), request.getBailerol()) && request.getBailerol() != null
				&& !request.getBailerol().isBlank()) {
			user.setBailerol(request.getBailerol());
		}
		try {
			userRepository.save(user);
			UserDto userDto = new UserDto(user);
			response = ResponseEntity.ok(userDto);
		} catch (Exception e) {
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return response;
	}

	/**
	 * Elimina un usuario por su ID.
	 *
	 * @param id El ID del usuario que se va a eliminar.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@Override
	@Transactional
	public ResponseEntity<GeneralResponseDto> deleteUser(Long id) throws UserNotFoundException {
		GeneralResponseDto responseDto;
		try {
			User user = userRepository.findById(id).orElseThrow(
					() -> new UserNotFoundException("No se ha encontrado el usuario solicitado para borrar"));
			// Eliminar tokens asociados al usuario
			tokenRepository.deleteByUserId(id);
			// Eliminar registros de recuperación de contraseña asociados al usuario
			forgotPasswordRepository.deleteByUserId(id);
			// Eliminar el usuario de la base de datos
			userRepository.delete(user);

			// respuesta exitosa
			responseDto = new GeneralResponseDto(HttpStatus.OK, "Usuario eliminado con éxito");
		} catch (UserNotFoundException e) {
			responseDto = new GeneralResponseDto(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			// Manejo de cualquier otra excepción inesperada
			responseDto = new GeneralResponseDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al intentar eliminar el usuario");
		}
		return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
	}

	/**
	 * Busca un usuario por su token.
	 *
	 * @param token el token del usuario que se va a buscar
	 * @return ResponseEntity que contiene un UserDto si se encuentra el usuario
	 *         asociado al token
	 * @throws TokenNotFoundException si el token no se encuentra en la base de
	 *                                datos
	 */
	@Override
	public ResponseEntity<UserDto> findByToken(String token) throws TokenNotFoundException {
		Token jwt = tokenRepository.findByToken(token)
				.orElseThrow(() -> new TokenNotFoundException("No se ha encontrado el token enviado"));
		User user = jwt.getUser();
		UserDto userDto = new UserDto(user);

		return ResponseEntity.ok(userDto);
	}

}
