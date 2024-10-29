package com.bailaconsarabackend.dto;

import java.time.LocalDate;
import java.util.List;

import com.bailaconsarabackend.model.Taller;
import com.bailaconsarabackend.model.User_Taller;

/**
 * Clase DTO (Data Transfer Object) que representa un taller con sus propiedades
 * relevantes. Se utiliza para transferir datos entre diferentes capas de la
 * aplicación, especialmente para enviar información sobre talleres desde el
 * backend hacia el frontend.
 */
public class TallerDto {

	private Long id;

	private String nombre;

	private String modalidad;

	private String profesores;

	private LocalDate fecha;

	private String hora;

	private String lugar;

	private List<User_Taller> usuarios_talleres;

	/**
	 * Constructor vacío de TallerDto.
	 */
	public TallerDto() {
	}

	/**
	 * Constructor de TallerDto que inicializa el objeto con todos los atributos
	 * necesarios.
	 *
	 * @param id                identificador único del taller.
	 * @param nombre            nombre del taller.
	 * @param modalidad         modalidad o estilo del taller.
	 * @param profesores        profesores o instructores del taller.
	 * @param fecha             fecha en la que se realiza el taller.
	 * @param hora              hora de inicio del taller.
	 * @param lugar             lugar donde se realiza el taller.
	 * @param usuarios_talleres lista de usuarios registrados en el taller.
	 */
	public TallerDto(Long id, String nombre, String modalidad, String profesores, LocalDate fecha, String hora,
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

	public TallerDto(Long id, String nombre, String modalidad, String profesores, LocalDate fecha, String hora,
			String lugar) {
		this.id = id;
		this.nombre = nombre;
		this.modalidad = modalidad;
		this.profesores = profesores;
		this.fecha = fecha;
		this.hora = hora;
		this.lugar = lugar;
	}

	/**
	 * Constructor de TallerDto que crea un objeto TallerDto a partir de un objeto
	 * Taller existente.
	 *
	 * @param taller objeto Taller del cual se extraerán los datos para crear el
	 *               TallerDto.
	 */
	public TallerDto(Taller taller) {
		this.id = taller.getId();
		this.nombre = taller.getNombre();
		this.modalidad = taller.getModalidad();
		this.profesores = taller.getProfesores();
		this.fecha = taller.getFecha();
		this.hora = taller.getHora();
		this.lugar = taller.getLugar();
		this.usuarios_talleres = taller.getUsuarios_talleres();
	}

	/*
	 * Getters y Setters
	 */
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
