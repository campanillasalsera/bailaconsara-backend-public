package com.bailaconsarabackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa un día específico en el que se abre una sala incluyendo los
 * generos musicales que pone ese día. Esta entidad se utiliza para asociar un
 * día de apertura, un género musical y una sala de baile, permitiendo gestionar
 * las actividades de baile de manera eficiente.
 */
@Entity
@Table(name = "horarios_sala")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HorarioSala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Sala de baile en la que se ofrece la actividad.
	 */
	@ManyToOne
	@JoinColumn(name = "sala_baile_id")
	private SalaBaile salaBaile;

	/**
	 * Día de apertura de la sala.
	 */
	@ManyToOne
	@JoinColumn(name = "dia_apertura_id")
	private DiaApertura diaApertura;

	/**
	 * Género musical que pone la sala de baile.
	 */
	@ManyToOne
	@JoinColumn(name = "genero_musical_id")
	private GeneroMusical generoMusical;

	/**
	 * Constructor vacío de HorarioSala.
	 */
	public HorarioSala() {
	}

	/**
	 * Constructor de HorarioSala que inicializa el objeto con todos los atributos
	 * necesarios.
	 *
	 * @param id            identificador único del horario de sala.
	 * @param salaBaile     sala de baile en la que se ofrece la actividad.
	 * @param diaApertura   día de apertura en el que se ofrece la actividad de
	 *                      baile.
	 * @param generoMusical género musical que se enseña durante la actividad de
	 *                      baile.
	 */
	public HorarioSala(Long id, SalaBaile salaBaile, DiaApertura diaApertura, GeneroMusical generoMusical) {
		this.id = id;
		this.salaBaile = salaBaile;
		this.diaApertura = diaApertura;
		this.generoMusical = generoMusical;
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

	public SalaBaile getSalaBaile() {
		return salaBaile;
	}

	public void setSalaBaile(SalaBaile salaBaile) {
		this.salaBaile = salaBaile;
	}

	public DiaApertura getDiaApertura() {
		return diaApertura;
	}

	public void setDiaApertura(DiaApertura diaApertura) {
		this.diaApertura = diaApertura;
	}

	public GeneroMusical getGeneroMusical() {
		return generoMusical;
	}

	public void setGeneroMusical(GeneroMusical generoMusical) {
		this.generoMusical = generoMusical;
	}

}
