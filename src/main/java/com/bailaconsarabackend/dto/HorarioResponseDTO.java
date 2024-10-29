package com.bailaconsarabackend.dto;

import java.util.List;

import com.bailaconsarabackend.model.Dias;
import com.bailaconsarabackend.model.GenerosMusicales;

/**
 * Clase DTO (Data Transfer Object) que representa la respuesta del horario para
 * una sala de baile. Contiene información detallada sobre la sala, incluido su
 * ID, nombre, localidad, dirección, el día de la semana y los géneros musicales
 * ofrecidos.
 */
public class HorarioResponseDTO {
	private Long salaId;
	private String nombreSala;
	private String localidad;
	private String address;
	private Dias dia;
	private List<GenerosMusicales> generos;

	/**
	 * Constructor por defecto de HorarioResponseDTO sin parámetros.
	 */
	public HorarioResponseDTO() {
	}

	/**
	 * Constructor de HorarioResponseDTO que inicializa el objeto con todos los
	 * atributos necesarios.
	 *
	 * @param salaId     el identificador único de la sala de baile.
	 * @param nombreSala el nombre de la sala de baile.
	 * @param localidad  la localidad donde se encuentra la sala de baile.
	 * @param address    la dirección completa de la sala de baile.
	 * @param dia        el día de la semana para el cual se aplica el horario.
	 * @param generos    la lista de géneros musicales ofrecidos en la sala para el
	 *                   día especificado.
	 */
	public HorarioResponseDTO(Long salaId, String nombreSala, String localidad, String address, Dias dia,
			List<GenerosMusicales> generos) {
		this.salaId = salaId;
		this.nombreSala = nombreSala;
		this.localidad = localidad;
		this.address = address;
		this.dia = dia;
		this.generos = generos;
	}

	// Getters y Setters
	public String getNombreSala() {
		return nombreSala;
	}

	public void setNombreSala(String nombreSala) {
		this.nombreSala = nombreSala;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Dias getDia() {
		return dia;
	}

	public void setDia(Dias dia) {
		this.dia = dia;
	}

	public List<GenerosMusicales> getGeneros() {
		return generos;
	}

	public void setGeneros(List<GenerosMusicales> generos) {
		this.generos = generos;
	}

	public Long getId() {
		return salaId;
	}

	public void setSalaId(Long salaId) {
		this.salaId = salaId;
	}

}
