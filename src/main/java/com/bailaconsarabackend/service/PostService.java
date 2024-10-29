package com.bailaconsarabackend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.bailaconsarabackend.dto.BasicResponseDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.PostDto;
import com.bailaconsarabackend.dto.UpdatePostDto;
import com.bailaconsarabackend.exception.PostNotFoundException;
import com.bailaconsarabackend.exception.PostValidationFailedException;
import com.bailaconsarabackend.model.Post;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con
 * publicaciones (posts). Proporciona métodos para crear, actualizar, listar,
 * buscar, eliminar y validar datos de publicaciones.
 */
public interface PostService {

	/**
	 * Crea un nuevo post con la imagen de portada y los datos proporcionados.
	 *
	 * @param post          Los datos del nuevo post.
	 * @param imagenportada La imagen de portada del post.
	 * @param request       La solicitud HTTP.
	 * @return Un objeto {@link BasicResponseDto} con el resultado de la operación.
	 * @throws IOException Si ocurre un error al almacenar la imagen de portada.
	 */
	BasicResponseDto createNewPost(PostDto post, MultipartFile imagenportada, HttpServletRequest request)
			throws PostValidationFailedException, IOException;

	/**
	 * Actualiza un post existente con la imagen de portada y los datos
	 * proporcionados.
	 *
	 * @param post          Los datos del post a actualizar.
	 * @param imagenportada La imagen de portada del post. Puede ser nula si no se
	 *                      desea actualizar la imagen.
	 * @return Un objeto {@link BasicResponseDto} con el resultado de la operación.
	 */
	BasicResponseDto updatePost(UpdatePostDto post, MultipartFile imagenportada);

	/**
	 * Obtiene una lista de todas las publicaciones.
	 * 
	 * @return Lista de todas las publicaciones.
	 */
	List<Post> listarPosts();

	/**
	 * Busca una publicación por su identificador único.
	 * 
	 * @param id Identificador único de la publicación.
	 * @return ResponseEntity con la publicación encontrada o un mensaje de error si
	 *         no se encuentra.
	 * @throws PostNotFoundException Si no se encuentra ninguna publicación con el
	 *                               identificador especificado.
	 */
	ResponseEntity<Post> findById(Long id) throws PostNotFoundException;

	/**
	 * Elimina un post por su ID
	 * 
	 * @param id
	 * @return ResponseEntity con un mensaje de exito y el HttpStatus
	 * @throws PostNotFoundException
	 */
	ResponseEntity<GeneralResponseDto> deleteById(Long id) throws PostNotFoundException;

	/**
	 * Encuentra un Post por el slug
	 * 
	 * @param slug
	 * @return Post correspondiente al slug
	 * @throws PostNotFoundException
	 */
	Post findByUrl(String slug) throws PostNotFoundException;

	/**
	 * Valida que los datos del post cumple con la regla correspondiente.
	 *
	 * @param postDto Los datos del post a validar.
	 * @return true si los datos son válidos, false de lo contrario.
	 */
	boolean validarDatosPost(PostDto postDto);

	/**
	 * Busca una publicación por su título.
	 *
	 * @param title Título de la publicación.
	 * @return Un objeto {@link ResponseEntity} que contiene el post encontrado o un
	 *         mensaje de error si no se encuentra.
	 * @throws PostNotFoundException Si no se encuentra ninguna publicación con el
	 *                               título especificado.
	 */
	ResponseEntity<Post> findByTitle(String title) throws PostNotFoundException;

}
