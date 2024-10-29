package com.bailaconsarabackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bailaconsarabackend.model.ForgotPassword;
import com.bailaconsarabackend.model.User;

/**
 * Interfaz para el repositorio de recuperación de contraseña.
 */
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

	/**
	 * Busca una solicitud de recuperación de contraseña por OTP y usuario.
	 *
	 * @param otp  el OTP (One-Time Password) de la solicitud de recuperación de
	 *             contraseña
	 * @param user el usuario asociado a la solicitud de recuperación de contraseña
	 * @return un Optional que puede contener la solicitud de recuperación de
	 *         contraseña si se encuentra
	 */
	@Query("select forgotpassword from ForgotPassword forgotpassword where forgotpassword.otp = ?1 and forgotpassword.user = ?2")
	Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

	/**
	 * Busca una solicitud de recuperación de contraseña por usuario.
	 *
	 * @param user el usuario asociado a la solicitud de recuperación de contraseña
	 * @return un Optional que puede contener la solicitud de recuperación de
	 *         contraseña si se encuentra
	 */
	Optional<ForgotPassword> findByUser(User user);

	/**
	 * Elimina una solicitud de recuperación de contraseña por ID de usuario.
	 *
	 * @param id el ID del usuario cuya solicitud de recuperación de contraseña se
	 *           eliminará
	 */
	void deleteByUserId(Long id);

}