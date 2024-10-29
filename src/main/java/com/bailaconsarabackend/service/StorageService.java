package com.bailaconsarabackend.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz de servicio para gestionar el almacenamiento de archivos.
 * Proporciona métodos para almacenar, cargar y eliminar archivos en el sistema
 * de almacenamiento.
 */
public interface StorageService {

	/**
	 * Almacena el archivo proporcionado.
	 *
	 * @param file El archivo a almacenar.
	 * @return La ruta donde se almacenó el archivo.
	 * @throws IOException
	 */
	String store(MultipartFile file) throws IOException;

	/**
	 * Carga el archivo especificado por su nombre como un recurso.
	 *
	 * @param filename El nombre del archivo a cargar.
	 * @return El archivo cargado como un recurso.
	 */
	Resource loadAsResource(String filename);

	/**
	 * Elimina una imagen del almacenamiento.
	 *
	 * @param filename Nombre del archivo de la imagen a eliminar.
	 */
	void deleteImage(String filename);
}
