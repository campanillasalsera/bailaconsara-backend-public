package com.bailaconsarabackend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Clase de controlador de excepciones global que maneja excepciones comunes de
 * la aplicación y las convierte en respuestas HTTP con mensajes descriptivos y
 * códigos de estado específicos. Cada método maneja un tipo específico de
 * excepción y proporciona una respuesta adecuada.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Maneja la excepción UserNotFoundException.
	 *
	 * @param exception la excepción UserNotFoundException
	 * @return una respuesta de entidad con un mensaje de error
	 */
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException exception) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	/**
	 * Maneja excepciones de autenticación y devuelve una respuesta HTTP 401
	 * (Unauthorized).
	 *
	 * @param exception la excepción de autenticación lanzada
	 * @return una respuesta de entidad con un mensaje de error y un código de
	 *         estado HTTP 401 (Unauthorized)
	 */
	@ExceptionHandler(AuthenticationException.class) // Captura la excepción padre
	@ResponseStatus(HttpStatus.UNAUTHORIZED) // Establece el código de estado apropiado
	public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException exception) {
		String message;
		if (exception instanceof BadCredentialsException) {
			message = "Las credenciales proporcionadas son incorrectas.";
		} else if (exception instanceof DisabledException) {
			message = "La cuenta del usuario está deshabilitada.";
		} else if (exception instanceof LockedException) {
			message = "La cuenta del usuario está bloqueada.";
		} else {
			message = "Error de autenticación."; // Default message for other AuthenticationExceptions
		}
		ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED, message);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
	}

	/**
	 * Maneja excepciones de tipo TokenNotFoundException y devuelve una respuesta
	 * HTTP 404 (NOT FOUND).
	 * 
	 * @param exception la excepción TokenNotFoundException que se ha lanzado
	 * @return ResponseEntity con un cuerpo que contiene un mensaje de error y un
	 *         código de estado HTTP 404
	 */
	@ExceptionHandler(TokenNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> tokenNotFoundException(TokenNotFoundException exception) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	/**
	 * Maneja la excepción PostNotFoundException.
	 *
	 * @param exception la excepción PostNotFoundException
	 * @return una respuesta de entidad con un mensaje de error
	 */
	@ExceptionHandler(PostNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> postNotFoundException(PostNotFoundException exception) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	/**
	 * Maneja la excepción PostNotFoundException.
	 *
	 * @param exception la excepción PostNotFoundException
	 * @return una respuesta de entidad con un mensaje de error
	 */
	@ExceptionHandler(PostValidationFailedException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> PostValidationFailedException(PostValidationFailedException exception) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	/**
	 * Maneja la excepción TallerNotFoundException.
	 *
	 * @param exception la excepción TallerNotFoundException
	 * @return una respuesta de entidad con un mensaje de error
	 */
	@ExceptionHandler(TallerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> tallerNotFoundException(TallerNotFoundException exception) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	/**
	 * Maneja la excepción UserAlreadyExistsException.
	 *
	 * @param exception la excepción UserAlreadyExistsException
	 * @return una respuesta de entidad con un mensaje de error
	 */
	@ExceptionHandler(UserAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorMessage> userAlreadyExists(UserAlreadyExistsException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	}

	/**
	 * Maneja la excepción MethodArgumentNotValidException.
	 *
	 * @param ex      la excepción MethodArgumentNotValidException
	 * @param headers los encabezados de la respuesta
	 * @param status  el estado HTTP
	 * @param request la solicitud web
	 * @return una respuesta de entidad con los errores de validación
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, Object> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
