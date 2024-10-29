package com.bailaconsarabackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa la relación entre un usuario y un taller.
 */
@Entity
@Table(name = "user_taller")
public class User_Taller {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * Usuario del taller
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	/*
	 * Id de la pareja del usuario del taller
	 */
	private Long partnerId;

	/*
	 * taller
	 */
	@ManyToOne
	@JoinColumn(name = "taller_id")
	private Taller taller;

	/*
	 * Estado en el que se encuentra el usuario Se usa para saber si es lider en
	 * espera o con pareja, o follower en espera o con pareja.
	 */
	@Enumerated(EnumType.ORDINAL)
	private UserTallerEstado userTallerEstado;

	/**
	 * Constructor por defecto de la clase User_Taller.
	 */
	public User_Taller() {
	}

	/**
	 * Constructor de la clase User_Taller con todos los atributos.
	 *
	 * @param id               el identificador de la relación usuario-taller
	 * @param user             el usuario asociado
	 * @param partnerId        el identificador del compañero asociado
	 * @param taller           el taller asociado
	 * @param userTallerEstado el estado de la relación usuario-taller
	 */
	public User_Taller(Long id, User user, Long partnerId, Taller taller, UserTallerEstado userTallerEstado) {
		this.id = id;
		this.user = user;
		this.partnerId = partnerId;
		this.taller = taller;
		this.userTallerEstado = userTallerEstado;
	}

	/*
	 * Getters Y setters
	 */
	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public UserTallerEstado getUserTallerEstado() {
		return userTallerEstado;
	}

	public void setUserTallerEstado(UserTallerEstado userTallerEstado) {
		this.userTallerEstado = userTallerEstado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Taller getTaller() {
		return taller;
	}

	public void setTaller(Taller taller) {
		this.taller = taller;
	}

}
