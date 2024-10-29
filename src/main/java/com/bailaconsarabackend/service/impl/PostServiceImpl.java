package com.bailaconsarabackend.service.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bailaconsarabackend.dto.BasicResponseDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.PostDto;
import com.bailaconsarabackend.dto.UpdatePostDto;
import com.bailaconsarabackend.exception.PostNotFoundException;
import com.bailaconsarabackend.exception.PostValidationFailedException;
import com.bailaconsarabackend.model.Post;
import com.bailaconsarabackend.repository.PostRepository;
import com.bailaconsarabackend.service.PostService;
import com.bailaconsarabackend.service.StorageService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Implementación del servicio de publicaciones para gestionar operaciones
 * relacionadas con publicaciones (posts). Proporciona métodos para crear,
 * actualizar, listar, buscar, eliminar y validar datos de publicaciones.
 */
@Service
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final StorageService storageService;

	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	/**
	 * Constructor para la clase PostServiceImpl.
	 *
	 * @param postRepository Repositorio de publicaciones.
	 * @param storageService el servicio encargado del almacenamiento de archivos
	 */
	public PostServiceImpl(PostRepository postRepository, StorageService storageService) {
		this.postRepository = postRepository;
		this.storageService = storageService;
	}

	/**
	 * Crea un nuevo post con la imagen de portada y los datos proporcionados.
	 *
	 * @param postDto       Los datos del nuevo post.
	 * @param imagenportada La imagen de portada del post.
	 * @param request       La solicitud HTTP.
	 * @return Un objeto {@link BasicResponseDto} con el resultado de la operación.
	 * @throws IOException Si ocurre un error al almacenar la imagen de portada.
	 */
	@Override
	public BasicResponseDto createNewPost(PostDto postDto, MultipartFile imagenportada, HttpServletRequest request)
			throws PostValidationFailedException, IOException {
		BasicResponseDto response = new BasicResponseDto();
		HttpStatus status = HttpStatus.BAD_REQUEST;

		// Log inicial indicando el inicio del proceso de creación de un nuevo post
		logger.info("Iniciando la creación de un nuevo post: {}", postDto != null ? postDto.getTitle() : "Sin título");

		if (postDto == null || imagenportada == null || imagenportada.isEmpty()) {
			logger.warn("Datos inválidos: postDto o imagen de portada es nulo o vacío.");
			response.setMessage("Los datos del post no son válidos o la imagen de portada no es válida");
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
		} else if (postRepository.findByTitle(postDto.getTitle()).isPresent()) {
			logger.warn("Ya existe un post con el título: {}", postDto.getTitle());
			response.setMessage("Ya existe un post con este título: " + postDto.getTitle());
			response.setStatus(HttpStatus.CONFLICT);
		} else if (!validarDatosPost(postDto)) {
			logger.warn("Errores en la validación de los datos del post: {}", postDto.getTitle());
			manejarErroresValidacion(postDto, response);
		} else {
			try {
				// Log antes de almacenar la imagen
				logger.info("Guardando la imagen de portada para el post: {}", postDto.getTitle());

				String urlImagenPortada = subirImagenPortada(imagenportada); // Aquí obtienes la URL ya correcta

				// Log después de almacenar la imagen
				logger.info("Imagen de portada guardada correctamente: {}", urlImagenPortada);

				// Log antes de mapear y guardar el post
				logger.info("Creando y guardando el nuevo post: {}", postDto.getTitle());

				// Crear y guardar el post en la base de datos
				Post post = mapPostDtoToPost(postDto, urlImagenPortada);
				Post savedPost = postRepository.save(post);

				logger.info("Post guardado correctamente: ID={}, Título={}", savedPost.getId(), savedPost.getTitle());

				response.setData(savedPost);
				response.setMessage("Post guardado con éxito");
				response.setStatus(HttpStatus.CREATED);
				response.setUrl(urlImagenPortada);
			} catch (IOException e) {
				// Log en caso de error al almacenar la imagen
				logger.error("Error al subir la imagen de portada: {}", e.getMessage());
				response.setMessage("Error al subir la imagen de portada");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// Log final indicando que se ha completado el proceso de creación del post
		logger.info("Proceso de creación de post finalizado: {}", postDto != null ? postDto.getTitle() : "Sin título");

		return response;
	}

	/**
	 * Sube la imagen de portada y genera la URL de acceso.
	 *
	 * @param imagenportada La imagen de portada a subir.
	 * @param request       La solicitud HTTP.
	 * @return La URL de acceso a la imagen de portada subida.
	 * @throws IOException Si ocurre un error al almacenar la imagen de portada.
	 */
	private String subirImagenPortada(MultipartFile imagenportada) throws IOException {
		return storageService.store(imagenportada);
	}

	/**
	 * Valida que los datos del post cumple con la regla correspondiente.
	 *
	 * @param postDto Los datos del post a validar.
	 * @return true si los datos son válidos, false de lo contrario.
	 */
	@Override
	public boolean validarDatosPost(PostDto postDto) {
		boolean isValid;
		if (contieneEnlaceExterno(postDto.getTextoinfo()) && contieneEnlaceInterno(postDto.getTextoinfo())
				&& contieneTexto(postDto.getTituloseo(), postDto.getFraseclave())
				&& contieneTexto(postDto.getSlug(), postDto.getFraseclave())
				&& contieneTexto(postDto.getTitle(), postDto.getFraseclave())
				&& contieneTexto(postDto.getAltportada(), postDto.getFraseclave())) {
			isValid = true;
		} else {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Maneja las validaciones de los datos del PostDto y establece los mensajes de
	 * error y el estado correspondiente en la respuesta.
	 *
	 * Este método revisa si el contenido del PostDto cumple con ciertas
	 * condiciones: - Debe contener al menos un enlace interno y uno externo. - La
	 * frase clave debe estar presente en varios campos, como el título SEO, título
	 * principal, slug, y alt de la imagen.
	 * 
	 * Si alguna de estas condiciones no se cumple, se configura un mensaje de error
	 * en la respuesta y el estado de la respuesta se marca como `BAD_REQUEST`.
	 *
	 * @param postDto  El objeto PostDto que contiene los datos del post a validar.
	 * @param response El objeto BasicResponseDto donde se establecen los mensajes
	 *                 de error y el estado HTTP.
	 * @throws PostValidationFailedException Si ocurre algún error crítico durante
	 *                                       la validación.
	 */
	private void manejarErroresValidacion(PostDto postDto, BasicResponseDto response)
			throws PostValidationFailedException {
		if (!contieneEnlaceInterno(postDto.getTextoinfo())) {
			response.setMessage("Asegúrate de que existe algún enlace interno");
		} else if (!contieneEnlaceExterno(postDto.getTextoinfo())) {
			response.setMessage("Asegúrate de que existe algún enlace externo");
		} else if (!contieneTexto(postDto.getTituloseo(), postDto.getFraseclave())) {
			response.setMessage("Asegúrate de que la frase clave aparece en el título SEO: " + postDto.getTituloseo());
		} else if (!contieneTexto(postDto.getTitle(), postDto.getFraseclave())) {
			response.setMessage("Asegúrate de que la frase clave aparece en el título principal " + postDto.getTitle());
		} else if (!contieneTexto(postDto.getSlug(), postDto.getFraseclave())) {
			response.setMessage("Asegúrate de que la frase clave aparece en el slug");
		} else if (!contieneTexto(postDto.getAltportada(), postDto.getFraseclave())) {
			response.setMessage("Asegúrate de que la frase clave aparece en el alt de la imagen");
		} else {
			response.setMessage("Ha ocurrido un error en la validación de los datos del post");
		}
		response.setStatus(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Mapea un objeto PostDto a un objeto Post.
	 *
	 * @param postDto       Los datos del post.
	 * @param imagenPortada Url de la imagen de portada del post.
	 * @return El objeto Post mapeado.
	 */
	private Post mapPostDtoToPost(PostDto postDto, String imagenportada) {
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setTextoinfo(postDto.getTextoinfo());
		post.setTextoprograma1(postDto.getTextoprograma1());
		post.setTextoprograma2(postDto.getTextoprograma2());
		post.setTextodresscode(postDto.getTextodresscode());
		post.setTextoartistas(postDto.getTextoartistas());
		post.setTextodjs(postDto.getTextodjs());
		post.setTextopases(postDto.getTextopases());
		post.setCreated_at(postDto.getCreated_at());
		post.setFraseclave(postDto.getFraseclave());
		post.setTituloseo(postDto.getTituloseo());
		post.setSlug(postDto.getSlug());
		post.setMetadescripcion(postDto.getMetadescripcion());
		post.setImagenportada(imagenportada);
		post.setAltportada(postDto.getAltportada());

		return post;
	}

	/**
	 * Actualiza un post existente con los datos proporcionados.
	 *
	 * @param postDto       Los datos actualizados del post.
	 * @param imagenPortada La nueva imagen de portada del post. // * @param request
	 *                      La solicitud HTTP.
	 * @return BasicResponseDto con la respuesta de la actualización del post.
	 */
	@Override
	public BasicResponseDto updatePost(UpdatePostDto postDto, MultipartFile imagenportada) {
		BasicResponseDto response = new BasicResponseDto();
		Long id = postDto.getId();

		try {
			// Buscar el post existente
			Post post = postRepository.findById(id)
					.orElseThrow(() -> new PostNotFoundException("No se ha encontrado el post para actualizar"));

			// Si se proporciona una nueva imagen de portada, subirla y actualizar la URL
			if (imagenportada != null && !imagenportada.isEmpty()) {
				String url = storageService.store(imagenportada);
				post.setImagenportada(url);
			}

			// Actualizar los demás campos del post con los datos proporcionados
			actualizarCamposPost(post, postDto);

			// Guardar los cambios en el repositorio
			Post savedPost = postRepository.save(post);

			response.setData(savedPost);
			response.setMessage("Post actualizado con éxito");
			response.setStatus(HttpStatus.OK);

		} catch (PostNotFoundException e) {
			response.setMessage("¡Error: " + e.getMessage() + "!");
			response.setStatus(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			response.setMessage("¡Error al actualizar el post!");
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
		}

		return response;
	}

	/**
	 * Actualiza los campos del post con los datos proporcionados en el DTO.
	 *
	 * @param post    El post a actualizar.
	 * @param postDto Los datos actualizados del post.
	 */
	private void actualizarCamposPost(Post post, UpdatePostDto postDto) {
		if (!Objects.equals(post.getTitle(), postDto.getTitle()) && postDto.getTitle() != null
				&& !postDto.getTitle().isBlank()) {
			post.setTitle(postDto.getTitle());
		}
		if (!Objects.equals(post.getTextoinfo(), postDto.getTextoinfo()) && postDto.getTextoinfo() != null
				&& !postDto.getTextoinfo().isBlank()) {
			post.setTextoinfo(postDto.getTextoinfo());
		}
		if (!Objects.equals(post.getTextoprograma1(), postDto.getTextoprograma1())
				&& postDto.getTextoprograma1() != null && !postDto.getTextoprograma1().isBlank()) {
			post.setTextoprograma1(postDto.getTextoprograma1());
		}
		if (!Objects.equals(post.getTextoprograma2(), postDto.getTextoprograma2())
				&& postDto.getTextoprograma2() != null && !postDto.getTextoprograma2().isBlank()) {
			post.setTextoprograma2(postDto.getTextoprograma2());
		}
		if (!Objects.equals(post.getTextodresscode(), postDto.getTextodresscode())
				&& postDto.getTextodresscode() != null && !postDto.getTextodresscode().isBlank()) {
			post.setTextodresscode(postDto.getTextodresscode());
		}
		if (!Objects.equals(post.getTextoartistas(), postDto.getTextoartistas()) && postDto.getTextoartistas() != null
				&& !postDto.getTextoartistas().isBlank()) {
			post.setTextoartistas(postDto.getTextoartistas());
		}
		if (!Objects.equals(post.getTextodjs(), postDto.getTextodjs()) && postDto.getTextodjs() != null
				&& !postDto.getTextodjs().isBlank()) {
			post.setTextodjs(postDto.getTextodjs());
		}
		if (!Objects.equals(post.getTextopases(), postDto.getTextopases()) && postDto.getTextopases() != null
				&& !postDto.getTextopases().isBlank()) {
			post.setTextopases(postDto.getTextopases());
		}
		post.setCreated_at(postDto.getCreated_at());

		if (!Objects.equals(post.getFraseclave(), postDto.getFraseclave()) && postDto.getFraseclave() != null
				&& !postDto.getFraseclave().isBlank()) {
			post.setFraseclave(postDto.getFraseclave());
		}
		if (!Objects.equals(post.getTituloseo(), postDto.getTituloseo()) && postDto.getTituloseo() != null
				&& !postDto.getTituloseo().isBlank()) {
			post.setTituloseo(postDto.getTituloseo());
		}
		if (!Objects.equals(post.getSlug(), postDto.getSlug()) && postDto.getSlug() != null
				&& !postDto.getSlug().isBlank()) {
			post.setSlug(postDto.getSlug());
		}
		if (!Objects.equals(post.getMetadescripcion(), postDto.getMetadescripcion())
				&& postDto.getMetadescripcion() != null && !postDto.getMetadescripcion().isBlank()) {
			post.setMetadescripcion(postDto.getMetadescripcion());
		}
		if (!Objects.equals(post.getAltportada(), postDto.getAltportada()) && postDto.getAltportada() != null
				&& !postDto.getAltportada().isBlank()) {
			post.setAltportada(postDto.getAltportada());
		}
	}

	/**
	 * Implementación del servicio para listar todos los posts (public).
	 *
	 * @return Lista de posts publica.
	 */
	@Override
	public List<Post> listarPosts() {
		return postRepository.findAll();
	}

	/**
	 * Implementación del servicio para buscar un post por su ID.
	 *
	 * @param id ID del post a buscar.
	 * @return ResponseEntity con una respuesta que contiene el post encontrado.
	 * @throws PostNotFoundException Si no se encuentra ningún post con el ID
	 *                               especificado.
	 */
	@Override
	public ResponseEntity<Post> findById(Long id) throws PostNotFoundException {
		Optional<Post> optionalPost = postRepository.findById(id);
		Post post = optionalPost.orElseThrow(() -> new PostNotFoundException("Post no encontrado"));
		return ResponseEntity.ok(post);
	}

	/**
	 * Encuentra un post por su URL (slug).
	 *
	 * @param slug La URL del post a buscar.
	 * @return El post encontrado.
	 * @throws PostNotFoundException Si no se encuentra ningún post con la URL
	 *                               especificada.
	 */
	@Override
	public Post findByUrl(String slug) throws PostNotFoundException {
		Post post = postRepository.findBySlug(slug).orElseThrow(() -> new PostNotFoundException("Post no encontrado"));
		return post;
	}

	/**
	 * Elimina un post por su ID y elimina su imagen asociada si existe.
	 *
	 * @param id El ID del post a eliminar.
	 * @return Un ResponseEntity con el estado de la operación y un mensaje.
	 * @throws PostNotFoundException Si no se encuentra ningún post con el ID
	 *                               especificado.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> deleteById(Long id) throws PostNotFoundException {
		Optional<Post> optionalPost = postRepository.findById(id);
		Post post = optionalPost.orElseThrow(() -> new PostNotFoundException("Post no encontrado"));

		String imageName = post.getImagenportada();

		// Extraer solo el nombre del archivo si la imagen está almacenada como URL
		if (imageName != null && imageName.startsWith("http")) {
			imageName = Paths.get(URI.create(imageName).getPath()).getFileName().toString();
		}

		if (imageName != null && !imageName.isEmpty()) {
			storageService.deleteImage(imageName);
		}
		postRepository.delete(post);
		GeneralResponseDto response = new GeneralResponseDto(HttpStatus.OK, "Post eliminado.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Comprueba si un texto contiene algún enlace externo.
	 *
	 * @param texto El texto a verificar.
	 * @return true si el texto contiene un enlace externo, false de lo contrario.
	 */
	public boolean contieneEnlaceExterno(String texto) {
		// Expresión regular para detectar enlaces HTTP o HTTPS
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(texto);
		return matcher.find();
	}

	/**
	 * Comprueba si un texto contiene algún enlace interno.
	 *
	 * @param texto El texto a verificar.
	 * @return true si el texto contiene un enlace interno, false de lo contrario.
	 */
	public boolean contieneEnlaceInterno(String texto) {
		String regex = "\\/[a-zA-Z0-9\\-._~:/?#@!$&'()*+,;=]+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(texto);
		return matcher.find();
	}

	/**
	 * Comprueba si un texto contiene otro texto buscado.
	 *
	 * @param texto        El texto en el que buscar.
	 * @param textoBuscado El texto a buscar.
	 * @return true si el texto contiene el texto buscado, false de lo contrario.
	 */
	public boolean contieneTexto(String texto, String textoBuscado) {
		if (texto == null || textoBuscado == null) {
			return false;
		}
		return texto.toLowerCase().contains(textoBuscado.toLowerCase());
	}

	/**
	 * Busca un post por su título en el repositorio y lo retorna envuelto en un
	 * ResponseEntity.
	 *
	 * @param title El título del post que se quiere buscar.
	 * @return ResponseEntity que contiene el post encontrado.
	 * @throws PostNotFoundException si no se encuentra un post con el título
	 *                               especificado.
	 */
	@Override
	public ResponseEntity<Post> findByTitle(String title) throws PostNotFoundException {

		Post post = postRepository.findByTitle(title)
				.orElseThrow(() -> new PostNotFoundException("Post no encontrado"));

		return ResponseEntity.ok(post);
	}

}
