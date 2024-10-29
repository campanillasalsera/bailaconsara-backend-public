package com.bailaconsarabackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bailaconsarabackend.model.User_Taller;

/**
 * Repositorio para gestionar operaciones CRUD de relaciones entre usuarios y
 * talleres.
 */
public interface User_TallerRepository extends JpaRepository<User_Taller, Long> {

	/**
	 * Busca todas las relaciones usuario-taller por el ID del taller.
	 *
	 * @param tallerId el ID del taller para el que se buscan las relaciones
	 *                 usuario-taller
	 * @return una lista de relaciones usuario-taller encontradas
	 */
	List<User_Taller> findByTallerId(Long tallerId);

	/**
	 * Busca una relación usuario-taller por el ID del usuario.
	 *
	 * @param userId el ID del usuario para el que se busca la relación
	 *               usuario-taller
	 * @return un Optional que puede contener la relación usuario-taller si se
	 *         encuentra
	 */
	Optional<User_Taller> findByUserId(Long userId);

	/**
	 * Busca un usuario asociado a un taller específico basándose en el ID del
	 * taller y el ID del usuario.
	 *
	 * @param tallerId El ID del taller.
	 * @param userId   El ID del usuario.
	 * @return Un Optional que contiene el User_Taller si existe; de lo contrario,
	 *         un Optional vacío.
	 */
	Optional<User_Taller> findByTallerIdAndUserId(Long tallerId, Long userId);

}
