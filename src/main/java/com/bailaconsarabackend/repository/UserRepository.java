package com.bailaconsarabackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bailaconsarabackend.model.Role;
import com.bailaconsarabackend.model.User;

/**
 * Repositorio para gestionar operaciones CRUD de usuarios.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Busca un usuario por su dirección de correo electrónico.
	 *
	 * @param email la dirección de correo electrónico del usuario a buscar
	 * @return un Optional que puede contener el usuario si se encuentra
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Busca usuarios por su rol.
	 *
	 * @param role el rol de los usuarios a buscar
	 * @return una lista de usuarios con el rol especificado
	 */
	List<User> findByRole(Role role);

}