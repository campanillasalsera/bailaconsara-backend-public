package com.bailaconsarabackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bailaconsarabackend.model.Post;

/**
 * Interfaz para el repositorio de publicaciones.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

	/**
	 * Busca una publicación por su slug.
	 *
	 * @param slug el slug de la publicación a buscar
	 * @return un Optional que puede contener la publicación si se encuentra
	 */
	Optional<Post> findBySlug(String slug);

	/**
	 * Busca una publicación por su título.
	 *
	 * @param title el título de la publicación a buscar
	 * @return un Optional que puede contener la publicación si se encuentra
	 */
	Optional<Post> findByTitle(String title);
}
