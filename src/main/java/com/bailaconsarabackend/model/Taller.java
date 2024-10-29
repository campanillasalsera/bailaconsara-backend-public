package com.bailaconsarabackend.model;

import java.time.LocalDate;
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
 * Clase que representa un taller.
 */
@Entity
@Table(name = "talleres")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Taller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private String modalidad;

	private String profesores;

	private LocalDate fecha;

	private String hora;

	private String lugar;

	/*
	 * lista de usuarios de un taller
	 */
	@OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User_Taller> usuarios_talleres;

	/**
	 * Constructor por defecto de la clase Taller.
	 */
	public Taller() {
	}

	/**
	 * Constructor de la clase Taller con todos los atributos.
	 *
	 * @param id                el identificador del taller
	 * @param nombre            el nombre del taller
	 * @param modalidad         la modalidad del taller
	 * @param profesores        los profesores del taller
	 * @param fecha             la fecha del taller
	 * @param hora              la hora del taller
	 * @param lugar             el lugar del taller
	 * @param usuarios_talleres la lista de usuarios asociados al taller
	 */
	public Taller(Long id, String nombre, String modalidad, String profesores, LocalDate fecha, String hora,
			String lugar, List<User_Taller> usuarios_talleres) {
		this.id = id;
		this.nombre = nombre;
		this.modalidad = modalidad;
		this.profesores = profesores;
		this.fecha = fecha;
		this.hora = hora;
		this.lugar = lugar;
		this.usuarios_talleres = usuarios_talleres;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<User_Taller> getUsuarios_talleres() {
		return usuarios_talleres;
	}

	public void setUsuarios_talleres(List<User_Taller> usuarios_talleres) {
		this.usuarios_talleres = usuarios_talleres;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}

	public String getProfesores() {
		return profesores;
	}

	public void setProfesores(String profesores) {
		this.profesores = profesores;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

}
