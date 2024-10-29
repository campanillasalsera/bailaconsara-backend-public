package com.bailaconsarabackend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.UserDto;
import com.bailaconsarabackend.exception.TokenNotFoundException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.User;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * Interfaz que define los servicios para la gestión de usuarios en el sistema.
 * Incluye operaciones como validación de tokens, búsqueda, actualización y
 * eliminación de usuarios, así como la gestión de roles y recuperación de
 * información relacionada con los usuarios.
 */
public interface UserService {

	/**
	 * Valida un token de usuario.
	 *
	 * @param theToken El token a validar.
	 * @return Un mensaje indicando el resultado de la validación.
	 * @throws ExpiredJwtException
	 * @throws TokenNotFoundException
	 */
	String validateToken(String theToken) throws ExpiredJwtException, TokenNotFoundException;

	/**
	 * Encuentra usuarios por su rol.
	 *
	 * @param role El rol de los usuarios a buscar.
	 * @return Una lista de usuarios con el rol especificado.
	 */
	List<User> findByRole(Role role);

	/**
	 * Lista todos los usuarios.
	 *
	 * @return Objeto BasicResponseDto que contiene la lista de usuarios.
	 */
	List<UserDto> listUsers();

	/**
	 * Elimina el token de un usuario.
	 *
	 * @param user El usuario cuyo token debe ser eliminado.
	 * @throws TokenNotFoundException
	 */
	void deleteUserToken(User user) throws TokenNotFoundException;

	/**
	 * Encuentra un usuario por su dirección de correo electrónico.
	 *
	 * @param email La dirección de correo electrónico del usuario a buscar.
	 * @return El usuario encontrado.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con la
	 *                               dirección de correo electrónico especificada.
	 */
	User findByEmail(String email) throws UserNotFoundException;

	/**
	 * Encuentra un usuario por su ID.
	 *
	 * @param id El ID del usuario a buscar.
	 * @return ResponseEntity con una respuesta que contiene el usuario encontrado.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               especificado.
	 */
	ResponseEntity<User> findById(Long id) throws UserNotFoundException;

	/**
	 * Actualiza la información de un usuario.
	 *
	 * @param request Los nuevos datos del usuario.
	 * @param userId  El ID del usuario a actualizar.
	 * @return ResponseEntity con una respuesta que contiene el usuario actualizado.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               especificado.
	 */
	ResponseEntity<UserDto> updateUser(UserDto request, Long userId) throws UserNotFoundException;

	/**
	 * Elimina un usuario.
	 *
	 * @param id El ID del usuario a eliminar.
	 * @return ResponseEntity con una respuesta que indica el resultado de la
	 *         operación de eliminación.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               especificado.
	 */
	ResponseEntity<GeneralResponseDto> deleteUser(Long id) throws UserNotFoundException;

	/**
	 * Busca un usuario por su token.
	 *
	 * @param token el token del usuario que se va a buscar
	 * @return ResponseEntity que contiene un UserDto si se encuentra el usuario
	 * @throws TokenNotFoundException si el token no se encuentra o no es válido
	 */
	ResponseEntity<UserDto> findByToken(String token) throws TokenNotFoundException;

}