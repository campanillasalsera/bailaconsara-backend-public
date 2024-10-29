package com.bailaconsarabackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.UserDto;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.service.UserService;

/**
 * Controlador para gestionar las operaciones relacionadas con talleres.
 * Proporciona endpoints para añadir, actualizar, eliminar y obtener información
 * de talleres, así como para gestionar inscripciones de usuarios.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	/**
	 * Constructor del controlador TallerController.
	 *
	 * @param tallerService Servicio para manejar la lógica de negocio de talleres.
	 */
	public UserController(UserService userService) {
		this.userService = userService;
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
	@PutMapping("update/{id}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto request, @PathVariable("id") Long userId)
			throws UserNotFoundException {
		return userService.updateUser(request, userId);
	}

	/**
	 * Elimina un usuario por su ID.
	 *
	 * @param id El ID del usuario que se va a eliminar.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@DeleteMapping("delete/{id}")
	public ResponseEntity<GeneralResponseDto> deleteUser(@PathVariable Long id) throws UserNotFoundException {
		return userService.deleteUser(id);
	}

}
