package com.bailaconsarabackend.service;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;

import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.HorarioResponseDTO;
import com.bailaconsarabackend.dto.SalaBaileRequestDTO;
import com.bailaconsarabackend.model.Dias;
import com.bailaconsarabackend.model.SalaBaile;

/**
 * Interfaz del servicio para gestionar operaciones relacionadas con Salas de
 * Baile. Incluye creación, actualización, eliminación y consulta de salas de
 * baile junto con sus horarios.
 */
public interface SalaBaileService {

	/**
	 * Crea una nueva sala de baile con sus horarios asociados.
	 *
	 * @param salaBaileDTO Datos de la sala de baile y horarios para crear.
	 * @return La sala de baile creada con sus horarios asociados.
	 * @throws NotFoundException
	 */
	SalaBaile createSalaConHorarios(SalaBaileRequestDTO salaBaileDTO) throws NotFoundException;

	/**
	 * Obtiene una lista de horarios disponibles por día de la semana.
	 *
	 * @param dia Día de la semana para filtrar los horarios.
	 * @return Lista de horarios disponibles para el día especificado.
	 */
	List<HorarioResponseDTO> getHorariosPorDia(Dias dia);

	/**
	 * Encuentra un DTO de solicitud de sala de baile por su ID.
	 *
	 * @param id El ID de la sala de baile a buscar.
	 * @return El DTO de solicitud de sala de baile correspondiente.
	 * @throws NotFoundException Si no se encuentra ninguna sala de baile con el ID
	 *                           proporcionado.
	 */
	SalaBaileRequestDTO findBySalaBaileId(Long id) throws NotFoundException;

	/**
	 * Actualiza una sala de baile existente con nuevos datos y horarios.
	 *
	 * @param id           El ID de la sala de baile a actualizar.
	 * @param salaBaileDTO Los nuevos datos de la sala de baile y horarios.
	 * @return La sala de baile actualizada con sus nuevos horarios.
	 * @throws NotFoundException Si no se encuentra ninguna sala de baile con el ID
	 *                           proporcionado.
	 */
	SalaBaile updateSalaConHorarios(Long id, SalaBaileRequestDTO salaBaileDTO) throws NotFoundException;

	/**
	 * Elimina una sala de baile junto con todos sus horarios asociados.
	 *
	 * @param id El ID de la sala de baile a eliminar.
	 * @return Un ResponseEntity indicando el resultado de la operación.
	 * @throws NotFoundException Si no se encuentra ninguna sala de baile con el ID
	 *                           proporcionado.
	 */
	ResponseEntity<GeneralResponseDto> deleteSalaConHorarios(Long id) throws NotFoundException;

}
