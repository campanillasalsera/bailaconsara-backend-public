package com.bailaconsarabackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bailaconsarabackend.dto.BasicResponseDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.TallerDto;
import com.bailaconsarabackend.dto.UserTallerDto;
import com.bailaconsarabackend.exception.TallerNotFoundException;
import com.bailaconsarabackend.exception.UserAlreadyExistsException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Taller;
import com.bailaconsarabackend.service.TallerService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los
 * talleres. Proporciona endpoints para añadir, actualizar, eliminar talleres y
 * gestionar la inscripción de usuarios.
 */
@RestController
@RequestMapping("/talleres")
public class TallerController {

	private final TallerService tallerService;

	public TallerController(TallerService tallerService) {
		this.tallerService = tallerService;
	}

	/**
	 * Añade un nuevo taller.
	 *
	 * @param tallerDto la información del taller a añadir
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@PostMapping("/admin/addTaller")
	public ResponseEntity<GeneralResponseDto> addTaller(@RequestBody TallerDto tallerDto) {
		return tallerService.addTaller(tallerDto);
	}

	/**
	 * Actualiza la información de un taller existente.
	 *
	 * @param tallerId  el ID del taller a actualizar
	 * @param tallerDto la información actualizada del taller
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@PutMapping("/admin/updateTaller/{tallerId}")
	public ResponseEntity<BasicResponseDto> updateTaller(@PathVariable("tallerId") Long tallerId,
			@RequestBody TallerDto tallerDto) {
		return tallerService.updateTaller(tallerDto, tallerId);
	}

	/**
	 * Elimina un taller existente.
	 *
	 * @param tallerId el ID del taller a eliminar
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@DeleteMapping("/admin/deleteTaller/{tallerId}")
	public ResponseEntity<GeneralResponseDto> deleteTaller(@PathVariable("tallerId") Long tallerId) {
		return tallerService.deleteTaller(tallerId);
	}

	/**
	 * Registra un usuario en un taller.
	 *
	 * @param tallerId       el ID del taller
	 * @param userId         el ID del usuario
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@PostMapping("/user/signInTaller/{tallerId}/{userId}")
	public ResponseEntity<GeneralResponseDto> signInUserTaller(@PathVariable("tallerId") Long tallerId,
			@PathVariable("userId") Long userId, final HttpServletRequest servletRequest)
			throws UserNotFoundException, TallerNotFoundException, UserAlreadyExistsException {
		return tallerService.signInUserTaller(tallerId, userId, servletRequest);
	}

	/**
	 * Registra una pareja de usuarios en un taller.
	 *
	 * @param tallerId       el ID del taller
	 * @param userId         el ID del usuario
	 * @param parejaEmail    el email de la pareja
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@PostMapping("/user/signInParejaTaller/{tallerId}/{userId}/{parejaEmail}")
	public ResponseEntity<GeneralResponseDto> signInParejaTaller(@PathVariable("tallerId") Long tallerId,
			@PathVariable("userId") Long userId, @PathVariable("parejaEmail") String parejaEmail,
			final HttpServletRequest servletRequest)
			throws UserNotFoundException, TallerNotFoundException, UserAlreadyExistsException {
		return tallerService.signInParejaTaller(tallerId, userId, parejaEmail, servletRequest);
	}

	/**
	 * Añade la pareja de un usuario a un taller.
	 *
	 * @param tallerId       el ID del taller
	 * @param userId         el ID del usuario
	 * @param parejaEmail    el email de la pareja
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@PostMapping("/user/addPartnerTaller/{tallerId}/{userId}/{parejaEmail}")
	public ResponseEntity<GeneralResponseDto> addPartnerTaller(@PathVariable("tallerId") Long tallerId,
			@PathVariable("userId") Long userId, @PathVariable("parejaEmail") String parejaEmail,
			final HttpServletRequest servletRequest) throws Exception {
		return tallerService.addPartnerTaller(tallerId, userId, parejaEmail, servletRequest);
	}

	/**
	 * Realiza la anulación de inscripción de un usuario en un taller.
	 *
	 * @param userTallerId   el ID de la asociación entre usuario y taller
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	@GetMapping("/user/signOutTaller/{tallerId}/{userId}")
	public ResponseEntity<GeneralResponseDto> signOutTaller(@PathVariable("tallerId") Long tallerId,
			@PathVariable("userId") Long userId, final HttpServletRequest servletRequest) throws Exception {
		return tallerService.signOutTaller(tallerId, userId, servletRequest);
	}

	/**
	 * Obtiene la lista de usuarios registrados en un taller.
	 *
	 * @param tallerId el ID del taller
	 * @return ResponseEntity con la lista de usuarios y su estado correspondiente
	 */
	@GetMapping("/admin/listUserTaller/{tallerId}")
	public ResponseEntity<List<UserTallerDto>> listUserTaller(@PathVariable("tallerId") Long tallerId) {
		return tallerService.getUsuariosByTallerId(tallerId);
	}

	/**
	 * Obtiene una lista de DTOs representando los talleres disponibles.
	 *
	 * @return ResponseEntity que contiene una lista de TallerDto con los detalles
	 *         de los talleres disponibles.
	 */
	@GetMapping("/user/listTalleres")
	public ResponseEntity<List<TallerDto>> listTalleres() {
		return tallerService.getListTalleres();
	}

	/**
	 * Obtiene un taller específico por su ID.
	 *
	 * @param tallerId el ID único del taller a buscar.
	 * @return ResponseEntity que contiene el Taller solicitado.
	 * @throws TallerNotFoundException si no se encuentra ningún taller con el ID
	 *                                 proporcionado.
	 */
	@GetMapping("/user/getTaller/{tallerId}")
	public ResponseEntity<Taller> getTallerById(@PathVariable("tallerId") Long tallerId)
			throws TallerNotFoundException {
		return tallerService.getTallerById(tallerId);
	}

	/**
	 * Verifica si un usuario está inscrito en un taller específico.
	 *
	 * @param tallerId el ID único del taller en el cual verificar la inscripción
	 *                 del usuario.
	 * @param userId   el ID único del usuario a verificar.
	 * @return true si el usuario está inscrito en el taller, false en caso
	 *         contrario.
	 * @throws Exception si ocurre algún error durante la verificación.
	 */
	@GetMapping("/user/isUserSignedUp/{tallerId}/{userId}")
	public boolean isUserSignedUp(@PathVariable("tallerId") Long tallerId, @PathVariable("userId") Long userId)
			throws Exception {
		return tallerService.isUserSignedUp(tallerId, userId);
	}

	/**
	 * Verifica si un usuario tiene pareja asignada en un taller específico.
	 *
	 * @param tallerId el ID único del taller en el cual verificar la asignación de
	 *                 pareja del usuario.
	 * @param userId   el ID único del usuario a verificar.
	 * @return true si el usuario tiene pareja asignada en el taller, false en caso
	 *         contrario.
	 * @throws Exception si ocurre algún error durante la verificación.
	 */
	@GetMapping("/user/isUserHasPartner/{tallerId}/{userId}")
	public boolean isUserHasPartner(@PathVariable("tallerId") Long tallerId, @PathVariable("userId") Long userId)
			throws Exception {
		return tallerService.isUserHasPartner(tallerId, userId);
	}

}
