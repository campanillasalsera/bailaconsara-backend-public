package com.bailaconsarabackend.controller;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.HorarioResponseDTO;
import com.bailaconsarabackend.dto.SalaBaileRequestDTO;
import com.bailaconsarabackend.model.Dias;
import com.bailaconsarabackend.model.SalaBaile;
import com.bailaconsarabackend.service.SalaBaileService;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las salas de
 * baile. Proporciona endpoints para crear salas con horarios, obtener horarios
 * por día, encontrar una sala por su ID, actualizar una sala y eliminar una
 * sala.
 */
@RestController
@RequestMapping("/salas")
public class SalaBaileController {

	private final SalaBaileService salaBaileService;

	/**
	 * Constructor de SalaBaileController que inyecta el servicio SalaBaileService.
	 *
	 * @param salaBaileService el servicio que contiene la lógica de negocio para
	 *                         las salas de baile.
	 */
	public SalaBaileController(SalaBaileService salaBaileService) {
		this.salaBaileService = salaBaileService;
	}

	/**
	 * Crea una nueva sala de baile con sus horarios asociados.
	 *
	 * @param salaBaileDTO el objeto DTO que contiene la información necesaria para
	 *                     crear la sala y sus horarios.
	 * @return la sala de baile creada con sus horarios asociados.
	 * @throws NotFoundException
	 */
	@PostMapping("create")
	public SalaBaile createSalaConHorarios(@RequestBody SalaBaileRequestDTO salaBaileDTO) throws NotFoundException {
		return salaBaileService.createSalaConHorarios(salaBaileDTO);
	}

	/**
	 * Obtiene los horarios disponibles en las salas de baile para un día
	 * específico.
	 *
	 * @param dia el día para el cual se quieren obtener los horarios.
	 * @return una lista de DTOs que representan los horarios disponibles en las
	 *         salas de baile para el día especificado.
	 */
	@GetMapping("/horarios/{dia}")
	public List<HorarioResponseDTO> getHorariosPorDia(@PathVariable Dias dia) {
		return salaBaileService.getHorariosPorDia(dia);
	}

	/**
	 * Encuentra y devuelve una sala de baile por su ID.
	 *
	 * @param id el ID único de la sala de baile a buscar.
	 * @return el DTO de la sala de baile encontrada por su ID.
	 * @throws NotFoundException si no se encuentra ninguna sala con el ID
	 *                           proporcionado.
	 */
	@GetMapping("horarios/getSala/{id}")
	public SalaBaileRequestDTO findPostById(@PathVariable("id") Long id) throws NotFoundException {
		return salaBaileService.findBySalaBaileId(id);
	}

	/**
	 * Actualiza la información de una sala de baile y sus horarios asociados.
	 *
	 * @param salaBaileDTO el objeto DTO con la nueva información de la sala y sus
	 *                     horarios.
	 * @param id           el ID único de la sala de baile a actualizar.
	 * @return la sala de baile actualizada con sus nuevos horarios asociados.
	 * @throws NotFoundException si no se encuentra ninguna sala con el ID
	 *                           proporcionado.
	 */
	@PutMapping("/updateSala/{id}")
	public SalaBaile updateSalaConHorarios(@RequestBody SalaBaileRequestDTO salaBaileDTO, @PathVariable("id") Long id)
			throws NotFoundException {
		return salaBaileService.updateSalaConHorarios(id, salaBaileDTO);
	}

	/**
	 * Elimina una sala de baile y sus horarios asociados por su ID.
	 *
	 * @param id el ID único de la sala de baile a eliminar.
	 * @return un ResponseEntity con un DTO genérico indicando el resultado de la
	 *         operación.
	 * @throws NotFoundException si no se encuentra ninguna sala con el ID
	 *                           proporcionado.
	 */
	@DeleteMapping("deleteSala/{id}")
	public ResponseEntity<GeneralResponseDto> deleteSalaConHorarios(@PathVariable("id") Long id)
			throws NotFoundException {
		return salaBaileService.deleteSalaConHorarios(id);
	}

}
