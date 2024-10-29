package com.bailaconsarabackend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bailaconsarabackend.dto.DiaGeneroDTO;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.HorarioResponseDTO;
import com.bailaconsarabackend.dto.SalaBaileRequestDTO;
import com.bailaconsarabackend.model.DiaApertura;
import com.bailaconsarabackend.model.Dias;
import com.bailaconsarabackend.model.GeneroMusical;
import com.bailaconsarabackend.model.GenerosMusicales;
import com.bailaconsarabackend.model.HorarioSala;
import com.bailaconsarabackend.model.SalaBaile;
import com.bailaconsarabackend.repository.DiaAperturaRepository;
import com.bailaconsarabackend.repository.GeneroMusicalRepository;
import com.bailaconsarabackend.repository.HorarioSalaRepository;
import com.bailaconsarabackend.repository.SalaBaileRepository;
import com.bailaconsarabackend.service.SalaBaileService;

import jakarta.transaction.Transactional;

/**
 * Implementación del servicio de salas de baile para gestionar operaciones
 * relacionadas con Salas de Baile. Incluye creación, actualización, eliminación
 * y consulta de salas de baile junto con sus horarios.
 */
@Service
public class SalaBaileServiceImpl implements SalaBaileService {

	private final SalaBaileRepository salaBaileRepository;
	private final DiaAperturaRepository diaAperturaRepository;
	private final GeneroMusicalRepository generoMusicalRepository;
	private final HorarioSalaRepository horarioSalaRepository;

	/**
	 * Constructor para la clase SalaBaileServiceImpl
	 * 
	 * @param salaBaileRepository     Repositorio de SalaBaile
	 * @param diaAperturaRepository   Repositorio de DiasApertura
	 * @param generoMusicalRepository Repositorio de GeneroMusical
	 * @param horarioSalaRepository   Repositorio de HorarioSala
	 */
	public SalaBaileServiceImpl(SalaBaileRepository salaBaileRepository, DiaAperturaRepository diaAperturaRepository,
			GeneroMusicalRepository generoMusicalRepository, HorarioSalaRepository horarioSalaRepository) {
		this.salaBaileRepository = salaBaileRepository;
		this.diaAperturaRepository = diaAperturaRepository;
		this.generoMusicalRepository = generoMusicalRepository;
		this.horarioSalaRepository = horarioSalaRepository;
	}

	/**
	 * Crea una sala de baile con sus horarios asociados.
	 *
	 * @param salaBaileDTO Datos de la sala de baile a crear.
	 * @return La sala de baile creada.
	 * @throws NotFoundException
	 */
	@Override
	public SalaBaile createSalaConHorarios(SalaBaileRequestDTO salaBaileDTO) throws NotFoundException {
		SalaBaile salaBaile = new SalaBaile();
		salaBaile.setNombreSala(salaBaileDTO.getNombreSala());
		salaBaile.setLocalidad(salaBaileDTO.getLocalidad());
		salaBaile.setAddress(salaBaileDTO.getAddress());

		List<HorarioSala> horarios = new ArrayList<>();

		for (DiaGeneroDTO diaGeneroDTO : salaBaileDTO.getDiasGeneros()) {

			try {
				DiaApertura diaApertura = getOrCreateDiaApertura(diaGeneroDTO.getDia());

				for (GenerosMusicales genero : diaGeneroDTO.getGeneros()) {

					GeneroMusical generoMusical = getOrCreateGeneroMusical(genero);

					HorarioSala horarioSala = new HorarioSala();
					horarioSala.setSalaBaile(salaBaile);
					horarioSala.setDiaApertura(diaApertura);
					horarioSala.setGeneroMusical(generoMusical);
					horarios.add(horarioSala);
				}
			} catch (NotFoundException e) {
				throw new NotFoundException();
			} catch (Exception e) {
				throw new RuntimeException("Error al crear la sala de baile", e);
			}
		}

		salaBaile.setHorariosSala(horarios);
		return salaBaileRepository.save(salaBaile);
	}

	/**
	 * Obtiene un {@link DiaApertura} existente o lo crea si no está presente en el
	 * repositorio.
	 * 
	 * @param dia El día de apertura que se está buscando.
	 * @return Un objeto {@link DiaApertura} correspondiente al día proporcionado.
	 * @throws NotFoundException Si no se puede encontrar o crear el
	 *                           {@link DiaApertura}.
	 */
	private DiaApertura getOrCreateDiaApertura(Dias dia) throws NotFoundException {
		return diaAperturaRepository.findByDia(dia).orElseGet(() -> {
			DiaApertura nuevoDiaApertura = new DiaApertura();
			nuevoDiaApertura.setDia(dia);
			return diaAperturaRepository.save(nuevoDiaApertura);
		});
	}

	/**
	 * Obtiene un {@link GeneroMusical} existente o lo crea si no está presente en
	 * el repositorio.
	 * 
	 * @param genero El género musical que se está buscando.
	 * @return Un objeto {@link GeneroMusical} correspondiente al género
	 *         proporcionado.
	 */
	private GeneroMusical getOrCreateGeneroMusical(GenerosMusicales genero) {
		GeneroMusical generoMusical = generoMusicalRepository.findByGenero(genero);
		if (generoMusical == null) {
			generoMusical = new GeneroMusical();
			generoMusical.setGenero(genero);
			generoMusical = generoMusicalRepository.save(generoMusical);
		}
		return generoMusical;
	}

	/**
	 * Obtiene los horarios de una sala de baile por día.
	 *
	 * @param dia El día para filtrar los horarios.
	 * @return Lista de horarios por día.
	 */
	@Override
	public List<HorarioResponseDTO> getHorariosPorDia(Dias dia) {
		List<HorarioResponseDTO> response = new ArrayList<>();

		if (dia == null) {
			throw new IllegalArgumentException("El día no puede ser nulo.");
		}

		try {
			// Obtener los horarios para el día especificado
			List<HorarioSala> horarios = horarioSalaRepository.findByDiaApertura_Dia(dia);

			if (horarios != null && !horarios.isEmpty()) {
				// Agrupar por sala
				Map<SalaBaile, List<HorarioSala>> groupedHorarios = horarios.stream()
						.collect(Collectors.groupingBy(HorarioSala::getSalaBaile));

				for (Map.Entry<SalaBaile, List<HorarioSala>> entry : groupedHorarios.entrySet()) {
					SalaBaile sala = entry.getKey();
					List<HorarioSala> salaHorarios = entry.getValue();

					HorarioResponseDTO dto = new HorarioResponseDTO();
					dto.setSalaId(sala.getId());
					dto.setNombreSala(sala.getNombreSala());
					dto.setLocalidad(sala.getLocalidad());
					dto.setAddress(sala.getAddress());
					dto.setDia(dia);
					dto.setGeneros(salaHorarios.stream().map(horario -> horario.getGeneroMusical().getGenero())
							.collect(Collectors.toList()));

					response.add(dto);
				}
			}
		} catch (DataAccessException e) {
			throw new ServiceException("Error al acceder a la base de datos. Intente nuevamente más tarde.", e);
		} catch (NullPointerException e) {
			throw new ServiceException("Ocurrió un error inesperado. Por favor, intente nuevamente.", e);
		} catch (Exception e) {
			throw new ServiceException("Error inesperado. Por favor, intente nuevamente.", e);
		}

		return response;
	}

	/**
	 * Busca una sala de baile por su ID.
	 *
	 * @param id El ID de la sala de baile.
	 * @return Los datos de la sala de baile encontrada.
	 * @throws NotFoundException Si no se encuentra la sala de baile.
	 */
	@Override
	public SalaBaileRequestDTO findBySalaBaileId(Long id) throws NotFoundException {
		SalaBaile sala = salaBaileRepository.findById(id).orElseThrow(() -> new NotFoundException());

		SalaBaileRequestDTO dto = new SalaBaileRequestDTO();
		dto.setId(sala.getId());
		dto.setNombreSala(sala.getNombreSala());
		dto.setLocalidad(sala.getLocalidad());
		dto.setAddress(sala.getAddress());

		// Mapa para agrupar los géneros musicales por día
		Map<Dias, List<GenerosMusicales>> diasGenerosMap = new HashMap<>();

		for (HorarioSala horario : sala.getHorariosSala()) {
			Dias dia = horario.getDiaApertura().getDia();
			GenerosMusicales generoMusical = horario.getGeneroMusical().getGenero();

			diasGenerosMap.computeIfAbsent(dia, diasGenerosList -> new ArrayList<>()).add(generoMusical);
		}

		// Convertir el mapa en una lista de DiaAperturaDTO
		List<DiaGeneroDTO> diasApertura = diasGenerosMap.entrySet().stream()
				.map(entry -> new DiaGeneroDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		dto.setDiasGeneros(diasApertura);

		return dto;
	}

	/**
	 * Actualiza una sala de baile con sus horarios asociados.
	 *
	 * @param id           El ID de la sala de baile a actualizar.
	 * @param salaBaileDTO Datos actualizados de la sala de baile.
	 * @return La sala de baile actualizada.
	 * @throws NotFoundException Si no se encuentra la sala de baile.
	 */
	@Override
	public SalaBaile updateSalaConHorarios(Long id, SalaBaileRequestDTO salaBaileDTO) throws NotFoundException {
		try {
			SalaBaile sala = salaBaileRepository.findById(id).orElseThrow(() -> new NotFoundException());

			// Actualizar campos si son diferentes y no están vacíos
			if (!Objects.equals(sala.getNombreSala(), salaBaileDTO.getNombreSala())
					&& salaBaileDTO.getNombreSala() != null && !salaBaileDTO.getNombreSala().isBlank()) {
				sala.setNombreSala(salaBaileDTO.getNombreSala());
			}
			if (!Objects.equals(sala.getLocalidad(), salaBaileDTO.getLocalidad()) && salaBaileDTO.getLocalidad() != null
					&& !salaBaileDTO.getLocalidad().isBlank()) {
				sala.setLocalidad(salaBaileDTO.getLocalidad());
			}
			if (!Objects.equals(sala.getAddress(), salaBaileDTO.getAddress()) && salaBaileDTO.getAddress() != null
					&& !salaBaileDTO.getAddress().isBlank()) {
				sala.setAddress(salaBaileDTO.getAddress());
			}

			// Obtener la lista de horarios existentes
			List<HorarioSala> horariosExistentes = sala.getHorariosSala();
			List<HorarioSala> nuevosHorarios = new ArrayList<>();

			// recorre los dias_Generos del request
			for (DiaGeneroDTO diaGeneroDTO : salaBaileDTO.getDiasGeneros()) {

				// Obtener o crear DiaApertura
				DiaApertura diaApertura = diaAperturaRepository.findByDia(diaGeneroDTO.getDia()).orElse(null);
				if (diaApertura == null) {
					diaApertura = new DiaApertura();
					diaApertura.setDia(diaGeneroDTO.getDia());
					diaApertura = diaAperturaRepository.save(diaApertura);
				}

				for (GenerosMusicales genero : diaGeneroDTO.getGeneros()) {
					// Obtener o crear GeneroMusical
					GeneroMusical generoMusical = generoMusicalRepository.findByGenero(genero);
					if (generoMusical == null) {
						generoMusical = new GeneroMusical();
						generoMusical.setGenero(genero);
						generoMusical = generoMusicalRepository.save(generoMusical);
					}

					// Crear nuevo HorarioSala
					HorarioSala horarioSala = new HorarioSala();
					horarioSala.setSalaBaile(sala);
					horarioSala.setDiaApertura(diaApertura);
					horarioSala.setGeneroMusical(generoMusical);
					nuevosHorarios.add(horarioSala);
				}
			}

			// Eliminar los horarios antiguos que no están en la nueva lista
			horariosExistentes.clear();

			// Agregar los nuevos horarios
			horariosExistentes.addAll(nuevosHorarios);

			sala.setHorariosSala(horariosExistentes);

			return salaBaileRepository.save(sala);

		} catch (DataAccessException e) {
			throw new ServiceException("Error al acceder a la base de datos.", e);
		} catch (Exception e) {
			throw new ServiceException("Error inesperado al actualizar la sala de baile.", e);
		}
	}

	/**
	 * Elimina una sala de baile junto con sus horarios asociados.
	 *
	 * @param id El ID de la sala de baile a eliminar.
	 * @return Una respuesta con el estado de la operación.
	 * @throws NotFoundException Si no se encuentra la sala de baile.
	 */
	@Transactional
	@Override
	public ResponseEntity<GeneralResponseDto> deleteSalaConHorarios(Long id) throws NotFoundException {
		GeneralResponseDto response = new GeneralResponseDto();

		try {
			SalaBaile sala = salaBaileRepository.findById(id).orElseThrow(() -> new NotFoundException());

			// Eliminar horarios asociados a la sala
			List<HorarioSala> horariosSala = sala.getHorariosSala();

			// Eliminar de la lista mientras se itera
			Iterator<HorarioSala> iterator = horariosSala.iterator();
			while (iterator.hasNext()) {
				HorarioSala horario = iterator.next();
				horarioSalaRepository.delete(horario);
				iterator.remove();
			}

			// Eliminar días de apertura asociados a la sala
			for (HorarioSala horario : horariosSala) {
				DiaApertura diaApertura = horario.getDiaApertura();
				if (diaApertura != null) {
					diaApertura.getHorariosSala().remove(horario);
					if (diaApertura.getHorariosSala().isEmpty()) {
						diaAperturaRepository.delete(diaApertura);
					}
				}
			}

			// Eliminar géneros musicales asociados a la sala
			for (HorarioSala horario : horariosSala) {
				GeneroMusical generoMusical = horario.getGeneroMusical();
				if (generoMusical != null) {
					generoMusical.getHorariosSala().remove(horario);
					if (generoMusical.getHorariosSala().isEmpty()) {
						generoMusicalRepository.delete(generoMusical);
					}
				}
			}

			// Finalmente, elimina la sala de baile
			salaBaileRepository.delete(sala);

		} catch (DataAccessException e) {
			throw new ServiceException("Error al acceder a la base de datos al eliminar la sala.", e);
		} catch (Exception e) {
			throw new ServiceException("Error inesperado al eliminar la sala de baile.", e);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
