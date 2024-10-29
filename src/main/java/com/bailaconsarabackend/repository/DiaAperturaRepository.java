package com.bailaconsarabackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bailaconsarabackend.model.DiaApertura;
import com.bailaconsarabackend.model.Dias;

/**
 * Repositorio de Spring Data JPA para gestionar operaciones CRUD relacionadas
 * con la entidad DiaApertura. Proporciona métodos para realizar consultas
 * personalizadas además de las operaciones CRUD básicas.
 */
public interface DiaAperturaRepository extends JpaRepository<DiaApertura, Long> {

	/**
	 * Busca y devuelve un DiaApertura basado en el día de la semana especificado.
	 *
	 * @param dia el día de la semana para buscar el {@link DiaApertura}
	 *            correspondiente.
	 * @return el DiaApertura encontrado o null si no se encuentra ninguno.
	 */
	Optional<DiaApertura> findByDia(Dias dia);

}