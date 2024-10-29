
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
 * Representa un día de apertura de una sala de baile. Utiliza JPA para mapear
 * la entidad a la tabla "dia_apertura" en la base de datos.
 * Usa @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
 * property = "id") para evitar referencias infinitas en el json devuelto
 */
@Entity
@Table(name = "dia_apertura")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DiaApertura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * Día de la semana que la sala abre
	 */
	@Enumerated(EnumType.STRING)
	private Dias dia;

	/*
	 * lista de días que abre una sala con sus respectivos generos músicales por
	 * cada día
	 * 
	 * HorarioSala es una clase de unión entre Horarios y Salas, porque varias salas
	 * pueden abrir varios días y a su vez poner varios generos musicales cada día.
	 */
	@OneToMany(mappedBy = "diaApertura", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HorarioSala> horariosSala;

	/**
	 * Constructor vacío de DiaApertura.
	 */
	public DiaApertura() {
	}

	/**
	 * Constructor de DiaApertura que inicializa el objeto con todos los atributos
	 * necesarios.
	 *
	 * @param id           identificador único del día de apertura.
	 * @param dia          día de la semana en el que la sala está abierta.
	 * @param horariosSala lista de horarios disponibles para este día de apertura.
	 */
	public DiaApertura(Long id, Dias dia, List<HorarioSala> horariosSala) {
		this.id = id;
		this.dia = dia;
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

	public Dias getDia() {
		return dia;
	}

	public void setDia(Dias dia) {
		this.dia = dia;
	}

	public List<HorarioSala> getHorariosSala() {
		return horariosSala;
	}

	public void setHorariosSala(List<HorarioSala> horariosSala) {
		this.horariosSala = horariosSala;
	}

}
