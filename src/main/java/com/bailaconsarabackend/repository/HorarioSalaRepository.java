package com.bailaconsarabackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bailaconsarabackend.model.Dias;
import com.bailaconsarabackend.model.HorarioSala;

/**
 * Repositorio de Spring Data JPA para gestionar operaciones CRUD relacionadas
 * con la entidad HorarioSala. Proporciona métodos para realizar consultas
 * personalizadas además de las operaciones CRUD básicas.
 */
public interface HorarioSalaRepository extends JpaRepository<HorarioSala, Long> {

	/**
	 * Busca y devuelve una lista de HorarioSala basados en el día de apertura
	 * especificado.
	 *
	 * @param dia el día de la semana para buscar los {@link HorarioSala}
	 *            correspondientes.
	 * @return una lista de HorarioSala encontrados o una lista vacía si no se
	 *         encuentran.
	 */
	List<HorarioSala> findByDiaApertura_Dia(Dias dia);

	/**
	 * Busca y devuelve una lista de HorarioSala basados en el ID de la sala de
	 * baile especificado.
	 *
	 * @param id el ID de la sala de baile para buscar los HorarioSala
	 *           correspondientes.
	 * @return una lista de HorarioSala encontrados o una lista vacía si no se
	 *         encuentran.
	 */
	List<HorarioSala> findBysalaBaileId(Long id);
}
