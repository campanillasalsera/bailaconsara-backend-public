package com.bailaconsarabackend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad para almacenar detalles de restablecimiento de contraseña olvidada.
 * Usa @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
 * property = "id") para evitar referencias infinitas en el json devuelto
 */
@Entity
@Table(name = "forgotpassword")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ForgotPassword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer otp;

	@Column(name = "expiration_time", columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime expirationTime;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	boolean verified = false;

	/**
	 * Constructor predeterminado.
	 */
	public ForgotPassword() {
	}

	/**
	 * Constructor para inicializar todos los campos.
	 *
	 * @param id             Identificador único.
	 * @param otp            Código único de un solo uso.
	 * @param expirationTime Tiempo de expiración del código.
	 * @param user           Usuario asociado al restablecimiento.
	 * @param verified       Estado de verificación.
	 */
	public ForgotPassword(Long id, Integer otp, LocalDateTime expirationTime, User user, boolean verified) {
		this.id = id;
		this.otp = otp;
		this.expirationTime = expirationTime;
		this.user = user;
		this.verified = verified;
	}

	/**
	 * Getters y Setters
	 */
	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}