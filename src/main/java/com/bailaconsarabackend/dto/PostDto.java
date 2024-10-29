package com.bailaconsarabackend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Clase DTO para representar un post en la aplicación. Contiene información
 * detallada sobre un post, incluyendo títulos, imágenes, textos y metadatos.
 */
public class PostDto {
	@NotBlank(message = "Introduce un título principal")
	private String title;

	@Size(min = 400, message = "Introduce un texto de al menos 400 caracteres")
	private String textoinfo;

	private String textoprograma1;
	private String textoprograma2;

	private String textodresscode;

	private String textoartistas;
	private String textodjs;

	private String textopases;

	private LocalDate created_at;

	@NotBlank(message = "Establece una frase objetivo de 4 palabras máximo")
	private String fraseclave;

	@NotBlank(message = "Establece un titulo seo que contenga la frase objetivo")
	private String tituloseo;

	@NotBlank(message = "Establece un slug que contenga la frase objetivo")
	private String slug;

	@Size(max = 130, message = "Establece una meta descripción que contenga la frase objetivo de 130 caracteres máximo")
	private String metadescripcion;

	@NotBlank(message = "Introduce una imagen de portada")
	private String imagenportada;

	@NotBlank(message = "Establece el alt para la imagen de portada que contenga la frase objetivo")
	private String altportada;

	/**
	 * Constructor sin argumentos para PostDto.
	 */
	public PostDto() {
	}

	/**
	 * Constructor con todos los campos para PostDto.
	 *
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
	public PostDto(@NotBlank(message = "Introduce un título principal") String title,
			@Size(min = 400, message = "Introduce un texto de al menos 400 caracteres") String textoinfo,
			String textoprograma1, String textoprograma2, String textodresscode, String textoartistas, String textodjs,
			String textopases, LocalDate created_at,
			@NotBlank(message = "Establece una frase objetivo de 4 palabras máximo") String fraseclave,
			@NotBlank(message = "Establece un titulo seo que contenga la frase objetivo") String tituloseo,
			@NotBlank(message = "Establece un slug que contenga la frase objetivo") String slug,
			@Size(max = 130, message = "Establece una meta descripción que contenga la frase objetivo de 130 caracteres máximo") String metadescripcion,
			@NotBlank(message = "Introduce una imagen de portada") String imagenportada,
			@NotBlank(message = "Establece el alt para la imagen de portada que contenga la frase objetivo") String altportada) {
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
