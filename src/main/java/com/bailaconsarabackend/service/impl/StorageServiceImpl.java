package com.bailaconsarabackend.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bailaconsarabackend.service.StorageService;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del servicio de almacenamiento utilizando FTP para la gestión
 * de archivos. Permite almacenar, cargar y eliminar archivos de forma remota en
 * un servidor FTP.
 */
@Service
public class StorageServiceImpl implements StorageService {

	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	// Configuraciones de FTP y frontend
	private final String ftpHost;
	private final int ftpPort;
	private final String ftpUser;
	private final String ftpPass;
	private final String ftpBaseDir;
	private final Path rootLocation;
	private final String frontendUrl;

	/**
	 * Constructor para inicializar las propiedades de configuración del servicio.
	 *
	 * @param mediaLocation Directorio raíz donde se almacenarán los archivos.
	 * @param frontendUrl   URL base del frontend.
	 * @param ftpHost       Dirección del servidor FTP.
	 * @param ftpPort       Puerto del servidor FTP.
	 * @param ftpUser       Usuario para acceder al servidor FTP.
	 * @param ftpPass       Contraseña del usuario FTP.
	 * @param ftpBaseDir    Directorio base en el servidor FTP para almacenar
	 *                      archivos.
	 */
	public StorageServiceImpl(@Value("${media.location}") String mediaLocation,
			@Value("${frontend.url}") String frontendUrl, @Value("${ftp.host}") String ftpHost,
			@Value("${ftp.port}") int ftpPort, @Value("${ftp.user}") String ftpUser,
			@Value("${ftp.password}") String ftpPass, @Value("${ftp.base-dir}") String ftpBaseDir) {
		this.rootLocation = Paths.get(mediaLocation);
		this.frontendUrl = frontendUrl;
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
		this.ftpUser = ftpUser;
		this.ftpPass = ftpPass;
		this.ftpBaseDir = ftpBaseDir;

		logger.info("Ruta de almacenamiento inicializada: {}", rootLocation);
		logger.info("URL del frontend: {}", frontendUrl);
		logger.info("Conectando a FTP: {}:{}", ftpHost, ftpPort);
	}

	/**
	 * Almacena un archivo en el servidor FTP.
	 *
	 * @param file El archivo a almacenar.
	 * @return La URL del archivo almacenado en el servidor frontend.
	 * @throws IOException si ocurre un error al almacenar el archivo.
	 */
	@Override
	public String store(MultipartFile file) throws IOException {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("No se puede almacenar un archivo vacío " + filename);
			}
			if (filename.contains("..")) {
				logger.error("Fallo al almacenar archivo con ruta inválida: {}", filename);
				throw new RuntimeException(
						"No se puede almacenar un archivo con una ruta relativa fuera del directorio actual "
								+ filename);
			}

			Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
			logger.info("Ruta del archivo de destino: {}", destinationFile);

			if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
				logger.error("Intento de almacenar el archivo fuera del directorio permitido: {}", destinationFile);
				throw new RuntimeException("No se puede almacenar el archivo fuera del directorio permitido");
			}

			// Conectar al servidor FTP y almacenar el archivo
			FTPClient ftpClient = new FTPClient();
			try {
				ftpClient.connect(ftpHost, ftpPort);
				logger.info("Conectado al servidor FTP: {}:{}", ftpHost, ftpPort);

				boolean loginSuccess = ftpClient.login(ftpUser, ftpPass);
				logger.info("Intento de inicio de sesión en FTP: {}", loginSuccess ? "exitoso" : "fallido");

				if (!loginSuccess) {
					throw new IOException("Fallo al iniciar sesión en el servidor FTP");
				}

				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // Aseguramos el modo binario
				logger.info("Modo pasivo y binario activados");

				// Leemos el contenido del archivo a un InputStream
				InputStream inputStream = new ByteArrayInputStream(file.getBytes());
				boolean done = ftpClient.storeFile(ftpBaseDir + filename, inputStream);

				inputStream.close();

				logger.info("Archivo almacenado en FTP: {}", done ? "exitoso" : "fallido");

				if (!done) {
					throw new IOException("Error al almacenar el archivo en el servidor FTP");
				}

				// Construir la URL del archivo en el servidor del frontend
				String fileUrl = frontendUrl + "/assets/img/" + filename;
				logger.info("Archivo {} almacenado correctamente en {}", filename, fileUrl);
				return fileUrl; // Devolver la URL correcta

			} catch (IOException e) {
				logger.error("Error al conectar o almacenar el archivo en el servidor FTP: {}", e.getMessage());
				throw new IOException("Error al almacenar el archivo en el servidor FTP", e);
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.logout();
						ftpClient.disconnect();
						logger.info("Desconexión de FTP exitosa");
					} catch (IOException ex) {
						logger.error("Error al desconectar FTP: {}", ex.getMessage());
					}
				}
			}

		} catch (IOException e) {
			logger.error("Error al almacenar el archivo {}: {}", filename, e.getMessage());
			throw new IOException("Error al almacenar el archivo " + filename, e);
		}
	}

	/**
	 * Carga un archivo como recurso desde la URL configurada.
	 *
	 * @param filename El nombre del archivo a cargar.
	 * @return UrlResource que apunta a la ubicación del archivo en el servidor
	 *         frontend.
	 * @throws RuntimeException si ocurre un error al intentar cargar el archivo.
	 */
	@Override
	public UrlResource loadAsResource(String filename) {
		try {
			String fileUrl = "https://bailaconsara.com/assets/img/" + filename;
			logger.info("Cargando archivo desde la URL: {}", fileUrl);
			return new UrlResource(fileUrl);
		} catch (MalformedURLException e) {
			logger.error("Error al cargar el archivo: {}", filename, e);
			throw new RuntimeException("Error al cargar el archivo: " + filename, e);
		}
	}

	/**
	 * Elimina un archivo en el servidor FTP.
	 *
	 * @param filename El nombre del archivo a eliminar.
	 * @throws RuntimeException si ocurre un error al intentar eliminar el archivo.
	 */
	@Override
	public void deleteImage(String filename) {
		FTPClient ftpClient = new FTPClient();

		try {
			ftpClient.connect(ftpHost, ftpPort);
			boolean loginSuccess = ftpClient.login(ftpUser, ftpPass);
			logger.info("Intento de inicio de sesión en FTP para eliminar archivo: {}",
					loginSuccess ? "exitoso" : "fallido");

			if (!loginSuccess) {
				throw new IOException("Fallo al iniciar sesión en el servidor FTP");
			}

			String remoteFile = ftpBaseDir + filename;
			logger.info("Eliminando el archivo remoto: {}", remoteFile);
			boolean deleted = ftpClient.deleteFile(remoteFile);
			ftpClient.logout();

			if (!deleted) {
				logger.error("Error al eliminar el archivo: {}", filename);
				throw new RuntimeException("Error al eliminar el archivo: " + filename);
			} else {
				logger.info("Archivo {} eliminado correctamente.", filename);
			}
		} catch (IOException e) {
			logger.error("Error al conectar al servidor FTP para eliminar archivo: {}", e.getMessage());
			throw new RuntimeException("Error al conectar al servidor FTP", e);
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
					logger.info("Desconexión de FTP exitosa después de eliminar archivo");
				}
			} catch (IOException ex) {
				logger.error("Error al desconectar FTP después de eliminar archivo: {}", ex.getMessage());
			}
		}
	}

//	//para saber donde guardar y de donde coger los archivos, mapeamos el media.location de app.properties
//	@Value("${media.location}")
//	public String mediaLocation;
//	
//	//declaramos un objeto path que será la ruta raiz de almacenamiento
//	public Path rootLocation;
//
//	
//    /**
//     * Inicializa la ruta raiz del almacenamiento.
//     *
//     * @throws IOException Si ocurre un error durante la creación de los directorios.
//     */
//	@Override
//	@PostConstruct
//	public void init() throws IOException {
//		
//		// inicializa la ruta raiz
//		rootLocation = Paths.get(mediaLocation);
//		
//		//Se asegura que esa ruta o carpeta exista antes de que se gestione algún archivo
//		Files.createDirectories(rootLocation);
//		
//	}
//
//	
//    /**
//     * Almacena el archivo proporcionado.
//     *
//     * @param file El archivo a almacenar.
//     * @return El nombre del archivo almacenado.
//     * @throws RuntimeException Si ocurre un error al guardar el archivo o si el archivo está vacío.
//     */
//	@Override
//	public String store(MultipartFile file) {
//
//		try {
//			if(file.isEmpty()) {
//				throw new RuntimeException("Error al almacenar un archivo vacío");
//			}
//			
//			//nombre original del archivo
//			String filename = file.getOriginalFilename();
//			
//			//ruta de almacenamiento
//			Path destinationFile = rootLocation.resolve(Paths.get(filename))
//					.normalize().toAbsolutePath();
//			
//			//almacena el archivo usando InputStream de la clase Util Files. Si ya hay un archivo
//			//con el mismo nombre, será reemplazado por el nuevo archivo.
//			try(InputStream inputStream = file.getInputStream()) {
//				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
//			}
//			
//			return filename;
//			
//		} catch (IOException e) {
//			throw new RuntimeException("Error al guardar el archivo", e);
//		}
//		
//	}
//	
//	
//	
//    /**
//     * Recupera un archivo a partir de su nombre como un recurso.
//     *
//     * @param filename El nombre del archivo a cargar.
//     * @return El archivo cargado como un recurso.
//     * @throws RuntimeException Si ocurre un error al leer el archivo o si el archivo no es legible.
//     */
//	@Override	
//	public Resource loadAsResource(String filename) {
//
//		try {
//			Path file = rootLocation.resolve(filename);
//			
//			Resource resource = new UrlResource((file.toUri()));
//			
//			if(resource.exists() || resource.isReadable()) {
//				return resource;
//			}else {
//				throw new RuntimeException("No se puede leer el archivo "+ filename);
//			}
//		} catch (MalformedURLException e) {
//			throw new RuntimeException("No se puede leer el archivo "+ filename);
//		}
//
//	}
//	
//	
//	
//	//TODO Este método no funciona en producción porque elimina un archivo local. 
//	/**
//	 * Elimina una imagen del sistema de archivos.
//	 * 
//	 * Este método busca y elimina un archivo en la ubicación especificada, si existe.
//	 * El nombre del archivo se resuelve con la ruta raíz configurada y se normaliza para evitar problemas de seguridad.
//	 *
//	 * @param filename el nombre del archivo a eliminar.
//	 * @throws RuntimeException si ocurre un error al intentar eliminar el archivo, como un problema de E/S.
//	 */
//	@Override
//	public void deleteImage(String filename) {
//	    try {
//	        Path file = rootLocation.resolve(filename).normalize().toAbsolutePath();
//	        Files.deleteIfExists(file);
//	    } catch (IOException e) {
//	        throw new RuntimeException("Error al eliminar el archivo " + filename, e);
//	    }
//	}
//	

}
