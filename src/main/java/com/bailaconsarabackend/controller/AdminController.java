package com.bailaconsarabackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.UserDto;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.service.AdminService;
import com.bailaconsarabackend.service.UserService;

/**
 * Controlador REST que gestiona las operaciones administrativas relacionadas
 * con la gestión de usuarios, incluyendo la obtención, actualización y
 * habilitación/deshabilitación de cuentas de usuario.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;
	private final AdminService adminService;

	/**
	 * Constructor para la clase AdminController.
	 *
	 * @param userService  Servicio de usuario.
	 * @param adminService Servicio de administrador.
	 * 
	 */
	public AdminController(UserService userService, AdminService adminService) {
		this.userService = userService;
		this.adminService = adminService;
	}

	/**
	 * Obtiene un usuario por su ID.
	 *
	 * @param id El ID del usuario que se va a obtener.
	 * @return ResponseEntity que contiene el usuario solicitado.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserNotFoundException {
		return userService.findById(id);
	}

	/**
	 * Lista todos los usuarios y envía una respuesta básica (lista,HpptStatus).
	 *
	 * @return ResponseEntity que contiene una lista de usuarios y el estatus.
	 */
	@GetMapping("/listUsers")
	public ResponseEntity<List<UserDto>> listUsers() {
		List<UserDto> usersListDto = new ArrayList<>();
		HttpStatus status = HttpStatus.NOT_FOUND;
		try {
			usersListDto = userService.listUsers();
			status = HttpStatus.OK;
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(usersListDto, status);
	}

	/**
	 * Actualiza el rol de un usuario.
	 *
	 * @param userId  El ID del usuario cuyo rol se va a actualizar.
	 * @param newRole El nuevo rol del usuario.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@PutMapping("role/{id}")
	public ResponseEntity<GeneralResponseDto> updateUserRole(@PathVariable("id") Long userId, @RequestBody Role newRole)
			throws UserNotFoundException {
		return adminService.updateUserRole(userId, newRole);
	}

	/**
	 * Deshabilita la cuenta de un usuario.
	 *
	 * @param userId El ID del usuario cuya cuenta se va a deshabilitar.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@PutMapping("user/disable/{id}")
	public ResponseEntity<GeneralResponseDto> disableUserAccount(@PathVariable("id") Long userId)
			throws UserNotFoundException {
		return adminService.disableUserAccount(userId);
	}

	/**
	 * Habilita la cuenta de un usuario.
	 *
	 * @param userId El ID del usuario cuya cuenta se va a habilitar.
	 * @return ResponseEntity que contiene una respuesta general.
	 * @throws UserNotFoundException Si no se encuentra ningún usuario con el ID
	 *                               proporcionado.
	 */
	@PutMapping("user/enable/{id}")
	public ResponseEntity<GeneralResponseDto> enableUserAccount(@PathVariable("id") Long userId)
			throws UserNotFoundException {
		return adminService.enableUserAccount(userId);
	}

}
