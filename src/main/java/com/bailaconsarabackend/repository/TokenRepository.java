package com.bailaconsarabackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bailaconsarabackend.model.Token;

/**
 * Repositorio para gestionar operaciones CRUD de tokens.
 */
public interface TokenRepository extends JpaRepository<Token, Long> {

	/**
	 * Busca un token por el ID de usuario.
	 *
	 * @param userId el ID del usuario asociado al token
	 * @return un Optional que puede contener el token si se encuentra
	 */
	@Query("""
			Select t from Token t inner join User u
			on t.user.id = u.id
			where t.user.id = :userId
			""")
	Optional<Token> findTokenByUser(Long userId);

	/**
	 * Busca un token por el ID de usuario que no ha sido cerrado.
	 *
	 * @param userId el ID del usuario asociado al token
	 * @return un Optional que puede contener el token si se encuentra
	 */
	@Query("""
			Select t from Token t inner join User u
			on t.user.id = u.id
			where t.user.id = :userId and t.loggedout = false
			""")
	Optional<Token> findTokenByUserNotLoggedout(Long userId);

	/**
	 * Busca un token por su valor.
	 *
	 * @param token el valor del token a buscar
	 * @return un Optional que puede contener el token si se encuentra
	 */
	Optional<Token> findByToken(String token);

	/**
	 * Elimina un token por el ID de usuario.
	 *
	 * @param id el ID del usuario cuyo token se eliminará
	 */
	void deleteByUserId(Long id);

}
