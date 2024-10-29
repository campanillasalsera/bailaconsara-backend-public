package com.bailaconsarabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bailaconsarabackend.model.GeneroMusical;
import com.bailaconsarabackend.model.GenerosMusicales;

/**
 * Repositorio de Spring Data JPA para gestionar operaciones CRUD relacionadas
 * con la entidad GeneroMusical. Proporciona métodos para realizar consultas
 * personalizadas además de las operaciones CRUD básicas.
 */
public interface GeneroMusicalRepository extends JpaRepository<GeneroMusical, Long> {

	/**
	 * Busca y devuelve un GeneroMusical basado en el género musical especificado.
	 *
	 * @param genero el género musical para buscar el GeneroMusical correspondiente.
	 * @return el GeneroMusical encontrado o null si no se encuentra ninguno.
	 */
	GeneroMusical findByGenero(GenerosMusicales genero);
}
