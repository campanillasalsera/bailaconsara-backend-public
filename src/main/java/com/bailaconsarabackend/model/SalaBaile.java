package com.bailaconsarabackend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Representa una sala de baile en el sistema. Esta entidad se utiliza para
 * almacenar información sobre las salas de baile disponibles, incluyendo
 * detalles como el nombre, la ubicación y la dirección física de la sala.
 * Además, gestiona los días en los que cada sala ofrece social de baile.
 */
@Entity
@Table(name = "salas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SalaBaile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombreSala;
	private String localidad;
	private String address;

	/**
	 * Lista de días en los que la sala de baile abre.
	 */
	@OneToMany(mappedBy = "salaBaile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HorarioSala> horariosSala;

	/**
	 * Constructor vacío de SalaBaile.
	 */
	public SalaBaile() {
	}

	/**
	 * Constructor de SalaBaile que inicializa el objeto con todos los atributos
	 * necesarios.
	 *
	 * @param id           identificador único de la sala de baile.
	 * @param nombreSala   nombre de la sala de baile.
	 * @param localidad    localidad o ciudad donde se encuentra la sala de baile.
	 * @param address      dirección completa de la sala de baile.
	 * @param horariosSala lista de horarios en los que la sala de baile ofrece
	 *                     clases o actividades de baile.
	 */
	public SalaBaile(Long id, String nombreSala, String localidad, String address, List<HorarioSala> horariosSala) {
		this.id = id;
		this.nombreSala = nombreSala;
		this.localidad = localidad;
		this.address = address;
		this.horariosSala = horariosSala;
	}

	/*
	 * Getters y Setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<HorarioSala> getHorariosSala() {
		return horariosSala;
	}

	public void setHorariosSala(List<HorarioSala> horariosSala) {
		this.horariosSala = horariosSala;
	}

}
