package com.bailaconsarabackend.dto;

import java.util.List;

/**
 * Clase DTO (Data Transfer Object) que representa una solicitud para crear o
 * actualizar una sala de baile. Contiene información detallada sobre la sala,
 * incluido su nombre, localidad, dirección y los días y géneros musicales
 * asociados a cada día.
 */
public class SalaBaileRequestDTO {
	private Long id;
	private String nombreSala;
	private String localidad;
	private String address;
	private List<DiaGeneroDTO> diasGeneros;

	/**
	 * Constructor por defecto de SalaBaileRequestDTO sin parámetros.
	 */
	public SalaBaileRequestDTO() {
	}

	/**
	 * Constructor de SalaBaileRequestDTO que inicializa el objeto con todos los
	 * atributos necesarios excepto el ID.
	 *
	 * @param nombreSala  el nombre de la sala de baile.
	 * @param localidad   la localidad donde se encuentra la sala de baile.
	 * @param address     la dirección completa de la sala de baile.
	 * @param diasGeneros la lista de objetos DiaGeneroDTO que representan los días
	 *                    de la semana y los géneros musicales asociados a cada día.
	 */
	public SalaBaileRequestDTO(String nombreSala, String localidad, String address, List<DiaGeneroDTO> diasGeneros) {
		this.nombreSala = nombreSala;
		this.localidad = localidad;
		this.address = address;
		this.diasGeneros = diasGeneros;
	}

	/**
	 * Constructor de SalaBaileRequestDTO que inicializa el objeto con todos los
	 * atributos necesarios, incluido el ID.
	 *
	 * @param id          el identificador único de la sala de baile.
	 * @param nombreSala  el nombre de la sala de baile.
	 * @param localidad   la localidad donde se encuentra la sala de baile.
	 * @param address     la dirección completa de la sala de baile.
	 * @param diasGeneros la lista de objetos DiaGeneroDTO que representan los días
	 *                    de la semana y los géneros musicales asociados a cada día.
	 */
	public SalaBaileRequestDTO(Long id, String nombreSala, String localidad, String address,
			List<DiaGeneroDTO> diasGeneros) {
		this.id = id;
		this.nombreSala = nombreSala;
		this.localidad = localidad;
		this.address = address;
		this.diasGeneros = diasGeneros;
	}

	/**
	 * Getters y Setters
	 */
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

	public List<DiaGeneroDTO> getDiasGeneros() {
		return diasGeneros;
	}

	public void setDiasGeneros(List<DiaGeneroDTO> diasGeneros) {
		this.diasGeneros = diasGeneros;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
