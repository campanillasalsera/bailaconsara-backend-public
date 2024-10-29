package com.bailaconsarabackend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Representa los generos musicales que pone una sala de baile. Esta entidad se
 * utiliza para categorizar y gestionar los géneros musicales, así como para
 * asociarlos con días específicos en las salas de baile.
 * 
 * Usa @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
 * property = "id") para evitar referencias infinitas en el json devuelto
 */
@Entity
@Table(name = "generos_musicales")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GeneroMusical {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private GenerosMusicales genero;

	/**
	 * Lista de días que abre una sala de baile con sus respectivos generos
	 * musicales por cada día.
	 * 
	 * HorarioSala es una clase de unión entre Horarios y Salas, ya que varias salas
	 * pueden abrir varios días y a su vez poner varios generos musicales cada día.
	 */
	@OneToMany(mappedBy = "generoMusical", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HorarioSala> horariosSala;

	/**
	 * Constructor vacío de GeneroMusical.
	 */
	public GeneroMusical() {
	}

	/**
	 * Constructor de GeneroMusical que inicializa el objeto con todos los atributos
	 * necesarios.
	 *
	 * @param id           identificador único del género musical.
	 * @param genero       enumeración que representa el tipo de género musical.
	 * @param horariosSala lista de horarios en los que este género musical está
	 *                     disponible en las salas de baile.
	 */
	public GeneroMusical(Long id, GenerosMusicales genero, List<HorarioSala> horariosSala) {
		this.id = id;
		this.genero = genero;
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

	public GenerosMusicales getGenero() {
		return genero;
	}

	public void setGenero(GenerosMusicales genero) {
		this.genero = genero;
	}

	public List<HorarioSala> getHorariosSala() {
		return horariosSala;
	}

	public void setHorariosSala(List<HorarioSala> horariosSala) {
		this.horariosSala = horariosSala;
	}

}
