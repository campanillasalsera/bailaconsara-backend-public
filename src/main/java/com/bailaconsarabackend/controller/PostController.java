package com.bailaconsarabackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bailaconsarabackend.dto.BasicResponseDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.PostDto;
import com.bailaconsarabackend.dto.UpdatePostDto;
import com.bailaconsarabackend.exception.PostNotFoundException;
import com.bailaconsarabackend.model.Post;
import com.bailaconsarabackend.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las
 * publicaciones (posts). Proporciona endpoints para crear, actualizar, listar y
 * eliminar publicaciones, así como buscar publicaciones por ID y slug.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final ObjectMapper objectMapper;

	/**
	 * Constructor de la clase PostController.
	 *
	 * @param postService  el servicio encargado de la lógica de negocio relacionada
	 *                     con las publicaciones
	 * @param objectMapper el objeto para mapear JSON a objetos Java y viceversa
	 */
	public PostController(PostService postService, ObjectMapper objectMapper) {
		this.postService = postService;
		this.objectMapper = objectMapper;
	}

	/**
	 * Crea un nuevo post con la imagen de portada y los datos proporcionados.
	 *
	 * @param imagenportada La imagen de portada del post.
	 * @param postData      Los datos del post en formato JSON.
	 * @param request       La solicitud HTTP.
	 * @return Una respuesta con el resultado de la operación.
	 */
	@PostMapping("/createPost")
	public ResponseEntity<BasicResponseDto> createNewPost(@RequestParam("imagenportada") MultipartFile imagenportada,
			@RequestParam("data") String postData, final HttpServletRequest request) {
		BasicResponseDto basicResponseDto = new BasicResponseDto();
		HttpStatus status;
		try {

			PostDto post = objectMapper.readValue(postData, PostDto.class);
			basicResponseDto = postService.createNewPost(post, imagenportada, request);
			status = basicResponseDto.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
			basicResponseDto.setMessage("Error al procesar la solicitud");
			basicResponseDto.setStatus(HttpStatus.BAD_REQUEST);
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(basicResponseDto, status);
	}

	/**
	 * Actualiza un post existente con la imagen de portada y los datos
	 * proporcionados.
	 *
	 * @param imagenportada La imagen de portada del post. Puede ser nula si no se
	 *                      desea actualizar la imagen.
	 * @param postData      Los datos del post en formato JSON.
	 * @param request       La solicitud HTTP.
	 * @return Una respuesta con el resultado de la operación.
	 */
	@PutMapping("/updatePost")
	public ResponseEntity<BasicResponseDto> updatePost(
			@RequestPart(value = "imagenportada", required = false) MultipartFile imagenportada,
			@RequestParam("data") String postData, HttpServletRequest request) {
		BasicResponseDto basicResponseDto = new BasicResponseDto();
		try {
			UpdatePostDto postDto = null;
			postDto = objectMapper.readValue(postData, UpdatePostDto.class);
			basicResponseDto = postService.updatePost(postDto, imagenportada);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
	}

	/**
	 * Obtiene una lista de todos los posts.
	 *
	 * @return Lista de posts.
	 */
	@GetMapping("/getPosts")
	public List<Post> listarPosts() {
		return postService.listarPosts();
	}

	/**
	 * Busca un post por su ID.
	 *
	 * @param id ID del post a buscar.
	 * @return El post con el ID especificado.
	 * @throws PostNotFoundException Si no se encuentra ningún post con el ID
	 *                               especificado.
	 */
	@GetMapping("getPost/{id}")
	public ResponseEntity<Post> findPostById(@PathVariable("id") Long id) throws PostNotFoundException {
		return postService.findById(id);
	}

	/**
	 * Elimina un post por su ID.
	 *
	 * @param id ID del post a eliminar.
	 * @return Una respuesta con el resultado de la operación.
	 * @throws PostNotFoundException Si no se encuentra ningún post con el ID
	 *                               especificado.
	 */
	@DeleteMapping("/deletePost/{id}")
	public ResponseEntity<GeneralResponseDto> deletePostById(@PathVariable("id") Long id) throws PostNotFoundException {
		return postService.deleteById(id);
	}

	/**
	 * Busca un post usando su slug.
	 *
	 * @param slug Slug del post a buscar.
	 * @return El post asociado al slug.
	 * @throws PostNotFoundException Si no se encuentra ningún post con el slug
	 *                               especificado.
	 */
	@GetMapping("/{slug}")
	public Post findByUrl(@PathVariable("slug") String slug) throws PostNotFoundException {
		System.out.println(slug);
		return postService.findByUrl(slug);
	}

	/**
	 * Busca un post por su título.
	 *
	 * @param title Título del post a buscar.
	 * @return El post con el título especificado.
	 * @throws PostNotFoundException Si no se encuentra ningún post con el título
	 *                               especificado.
	 */
	@GetMapping("post/{title}")
	public ResponseEntity<Post> findPostByTitle(@PathVariable("title") String title) throws PostNotFoundException {
		return postService.findByTitle(title);
	}

}
