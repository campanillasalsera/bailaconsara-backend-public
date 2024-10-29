package com.bailaconsarabackend.dto;

import java.time.LocalDate;

/**
 * Clase DTO para manejar la actualización de un post. Sin validaciones Incluye
 * información detallada sobre un post, incluyendo títulos, imágenes, textos y
 * metadatos.
 */
public class UpdatePostDto {

	private Long id;

	private String title;
	private String textoinfo;
	private String textoprograma1;
	private String textoprograma2;
	private String textodresscode;
	private String textoartistas;
	private String textodjs;
	private String textopases;
	private LocalDate created_at;
	private String fraseclave;
	private String tituloseo;
	private String slug;
	private String metadescripcion;
	private String imagenportada;
	private String altportada;

	/**
	 * Constructor sin argumentos para UpdatePostDto.
	 */
	public UpdatePostDto() {
	}

	/**
	 * Constructor con todos los campos para UpdatePostDto.
	 *
	 * @param id              ID del post a actualizar.
	 * @param title           Título principal del post.
	 * @param textoinfo       Texto informativo del post.
	 * @param textoprograma1  Texto del programa 1 del post.
	 * @param textoprograma2  Texto del programa 2 del post.
	 * @param textodresscode  Texto del dress code del post.
	 * @param textoartistas   Texto de los artistas del post.
	 * @param textodjs        Texto de los DJs del post.
	 * @param textopases      Texto de los pases del post.
	 * @param created_at      Fecha y hora de creación del post.
	 * @param fraseclave      Frase clave del post.
	 * @param tituloseo       Título SEO del post.
	 * @param slug            Slug del post.
	 * @param metadescripcion Meta descripción del post.
	 * @param imagenportada   Imagen de portada del post.
	 * @param altportada      Alt de la imagen de portada del post.
	 */
	public UpdatePostDto(Long id, String title, String textoinfo, String textoprograma1, String textoprograma2,
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
