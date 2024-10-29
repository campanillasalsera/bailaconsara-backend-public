package com.bailaconsarabackend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bailaconsarabackend.service.StorageService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador para la gestión de archivos multimedia. Permite la carga y
 * recuperación de archivos.
 */
@RestController
@RequestMapping("media")
public class MediaController {

	private final StorageService storageService;
	private final HttpServletRequest request;

	/**
	 * Constructor para inyectar las dependencias necesarias.
	 *
	 * @param storageService Servicio de almacenamiento de archivos.
	 * @param request        Objeto HttpServletRequest para obtener información de
	 *                       la solicitud.
	 */
	public MediaController(StorageService storageService, HttpServletRequest request) {
		this.storageService = storageService;
		this.request = request;
	}

	/**
	 * Maneja la carga de archivos y genera una URL para acceder al archivo cargado.
	 *
	 * @param multipartFile El archivo que se va a cargar.
	 * @return Un mapa que contiene la URL del archivo cargado.
	 * @throws IOException
	 */
	@PostMapping("upload")
	public Map<String, String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		String path = storageService.store(multipartFile);
		String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
		String url = ServletUriComponentsBuilder.fromHttpUrl(host).path("/media/").path(path).toUriString();

		return Map.of("url", url);
	}

	/**
	 * Recupera un archivo especificado por su nombre. El {filename:.+} es una
	 * expresión regular que asegura que el filename puede contener uno o más
	 * caracteres, incluidos los puntos.
	 *
	 * @param filename El nombre del archivo a recuperar.
	 * @return Un ResponseEntity que contiene el recurso del archivo y sus headers.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	@GetMapping("{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
		Resource file = storageService.loadAsResource(filename);
		String contentType = Files.probeContentType(file.getFile().toPath());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).body(file);
	}

}
