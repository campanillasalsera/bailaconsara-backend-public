package com.bailaconsarabackend.model;

/**
 * Enumeración que representa el estado de la relación entre un usuario y un
 * taller.
 */
public enum UserTallerEstado {

	/** El usuario es líder y está esperando que se le asigne una pareja. */
	LIDERWAITING,

	/** El usuario es seguidor y está esperando que se le asigne una pareja. */
	FOLLOWERWAITING,

	/** El usuario es líder y tienes pareja asignada para el taller. */
	LIDERCONFIRMED,

	/** El usuario es seguidor y tienes pareja asignada para el taller. */
	FOLLOWERCONFIRMED
}
