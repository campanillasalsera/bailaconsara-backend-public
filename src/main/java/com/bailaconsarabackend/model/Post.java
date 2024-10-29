package com.bailaconsarabackend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad para almacenar detalles de publicaciones.
 */
@Entity
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Column(length = 6000)
	private String textoinfo;

	@Column(length = 4000)
	private String textoprograma1;
	@Column(length = 1000)
	private String textoprograma2;

	@Column(length = 1000)
	private String textodresscode;

	@Column(length = 1000)
	private String textoartistas;
	@Column(length = 1000)
	private String textodjs;

	@Column(length = 1000)
	private String textopases;

	private LocalDate created_at;

	@Column(unique = true)
	private String fraseclave;
	private String tituloseo;
	private String slug;
	private String metadescripcion;
	private String imagenportada;
	private String altportada;

	/**
	 * Constructor predeterminado.
	 */
	public Post() {
	}

	/**
	 * Constructor para inicializar todos los campos.
	 *
	 * @param id                Identificador único.
	 * @param title             Título de la publicación.
	 * @param textoinfo         Texto informativo.
	 * @param subtituloprograma Subtítulo del programa relacionado.
	 * @param textoprograma1    Texto del programa 1.
	 * @param textoprograma2    Texto del programa 2.
	 * @param textodresscode    Texto del código de vestimenta.
	 * @param textoartistas     Texto sobre los artistas.
	 * @param textodjs          Texto sobre los DJs.
	 * @param textopases        Texto sobre los pases.
	 * @param created_at        Fecha y hora de creación.
	 * @param fraseclave        Frase clave única.
	 * @param tituloseo         Título SEO.
	 * @param slug              Slug para la URL.
	 * @param metadescripcion   Meta descripción.
	 * @param imagenportada     URL de la imagen de portada.
	 * @param altportada        ALT de la imagen de portada.
	 */
	public Post(Long id, String title, String textoinfo, String textoprograma1, String textoprograma2,
			String textodresscode, String textoartistas, String textodjs, String textopases, LocalDate created_at,
			String fraseclave, String tituloseo, String slug, String metadescripcion, String imagenportada,
			String altportada) {
		this.id = id;
		this.title = title;
		this.textoinfo = textoinfo;
		this.textoprograma1 = textoprograma1;
		this.textoprograma2 = textoprograma2;
		this.textodresscode = textodresscode;
		this.textoartistas = textoartistas;
		this.textodjs = textodjs;
		this.textopases = textopases;
		this.created_at = created_at;
		this.fraseclave = fraseclave;
		this.tituloseo = tituloseo;
		this.slug = slug;
		this.metadescripcion = metadescripcion;
		this.imagenportada = imagenportada;
		this.altportada = altportada;
	}

	/**
	 * Getters y Setters
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagenportada() {
		return imagenportada;
	}

	public void setImagenportada(String imagenportada) {
		this.imagenportada = imagenportada;
	}

	public String getTextoinfo() {
		return textoinfo;
	}

	public void setTextoinfo(String textoinfo) {
		this.textoinfo = textoinfo;
	}

	public String getTextoprograma1() {
		return textoprograma1;
	}

	public void setTextoprograma1(String textoprograma1) {
		this.textoprograma1 = textoprograma1;
	}

	public String getTextoprograma2() {
		return textoprograma2;
	}

	public void setTextoprograma2(String textoprograma2) {
		this.textoprograma2 = textoprograma2;
	}

	public String getTextodresscode() {
		return textodresscode;
	}

	public void setTextodresscode(String textodresscode) {
		this.textodresscode = textodresscode;
	}

	public String getTextoartistas() {
		return textoartistas;
	}

	public void setTextoartistas(String textoartistas) {
		this.textoartistas = textoartistas;
	}

	public String getTextodjs() {
		return textodjs;
	}

	public void setTextodjs(String textodjs) {
		this.textodjs = textodjs;
	}

	public String getTextopases() {
		return textopases;
	}

	public void setTextopases(String textopases) {
		this.textopases = textopases;
	}

	public LocalDate getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDate created_at) {
		this.created_at = created_at;
	}

	public String getFraseclave() {
		return fraseclave;
	}

	public void setFraseclave(String fraseclave) {
		this.fraseclave = fraseclave;
	}

	public String getTituloseo() {
		return tituloseo;
	}

	public void setTituloseo(String tituloseo) {
		this.tituloseo = tituloseo;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getMetadescripcion() {
		return metadescripcion;
	}

	public void setMetadescripcion(String metadescripcion) {
		this.metadescripcion = metadescripcion;
	}

	public String getAltportada() {
		return altportada;
	}

	public void setAltportada(String altportada) {
		this.altportada = altportada;
	}

}
