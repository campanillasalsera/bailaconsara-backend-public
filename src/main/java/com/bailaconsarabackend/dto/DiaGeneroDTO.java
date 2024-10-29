package com.bailaconsarabackend.dto;

import java.util.List;

import com.bailaconsarabackend.model.Dias;
import com.bailaconsarabackend.model.GenerosMusicales;

/**
 * Clase DTO (Data Transfer Object) que representa la combinación de un día de
 * la semana y una lista de géneros musicales asociados a ese día. Utilizado
 * principalmente para transferir datos entre capas de la aplicación.
 */
public class DiaGeneroDTO {
	private Dias dia;
	private List<GenerosMusicales> generos;

	/**
	 * Constructor por defecto de DiaGeneroDTO sin parámetros.
	 */
	public DiaGeneroDTO() {
	}

	/**
	 * Constructor de DiaGeneroDTO que inicializa el objeto con un día específico y
	 * una lista de géneros musicales.
	 *
	 * @param dia     el día de la semana para el cual se establecen los géneros
	 *                musicales.
	 * @param generos la lista de géneros musicales asociados al día especificado.
	 */
	public DiaGeneroDTO(Dias dia, List<GenerosMusicales> generos) {
		this.dia = dia;
		this.generos = generos;
	}

	// Getters y setters
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

}
