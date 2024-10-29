package com.bailaconsarabackend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bailaconsarabackend.dto.BasicResponseDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.TallerDto;
import com.bailaconsarabackend.dto.UserTallerDto;
import com.bailaconsarabackend.exception.TallerNotFoundException;
import com.bailaconsarabackend.exception.UserAlreadyExistsException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Taller;
import com.bailaconsarabackend.model.User_Taller;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con talleres y
 * usuarios en ellos. Incluye métodos para el registro de usuarios, gestión de
 * parejas, actualización y eliminación de talleres.
 */
public interface TallerService {

	/**
	 * Registra un usuario en un taller.
	 *
	 * @param tallerId      el ID del taller
	 * @param userId        el ID del usuario
	 * @param serverRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 * @throws UserNotFoundException      si el usuario no es encontrado
	 * @throws TallerNotFoundException    si el taller no es encontrado
	 * @throws UserAlreadyExistsException si el usuario ya está registrado en el
	 *                                    taller
	 */
	ResponseEntity<GeneralResponseDto> signInUserTaller(Long tallerId, Long userId, HttpServletRequest serverRequest)
			throws UserNotFoundException, TallerNotFoundException, UserAlreadyExistsException;

	/**
	 * Añade un nuevo taller.
	 *
	 * @param tallerDto la información del taller a añadir
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	ResponseEntity<GeneralResponseDto> addTaller(TallerDto tallerDto);

	/**
	 * Obtiene la lista de usuarios registrados en un taller.
	 *
	 * @param tallerId el ID del taller
	 * @return ResponseEntity con la lista de usuarios y su estado correspondiente
	 */
	ResponseEntity<List<UserTallerDto>> getUsuariosByTallerId(Long tallerId);

	/**
	 * Registra una pareja de un usuarios en un taller.
	 *
	 * @param tallerId       el ID del taller
	 * @param userId         el ID del usuario
	 * @param parejaEmail    el email de la pareja
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 * @throws UserNotFoundException      si el usuario no es encontrado
	 * @throws TallerNotFoundException    si el taller no es encontrado
	 * @throws UserAlreadyExistsException si la pareja ya está registrada en el
	 *                                    taller
	 */
	ResponseEntity<GeneralResponseDto> signInParejaTaller(Long tallerId, Long userId, String parejaEmail,
			HttpServletRequest servletRequest)
			throws UserNotFoundException, TallerNotFoundException, UserAlreadyExistsException;

	/**
	 * Realiza la anulación de un registro de asistencia de un usuario en un taller.
	 *
	 * @param tallerId      El identificador del taller del cual el usuario desea
	 *                      salir.
	 * @param userId        El identificador del usuario que realiza la acción de
	 *                      salida.
	 * @param serverRequest La solicitud HTTP que permite acceder a detalles del
	 *                      servidor, como encabezados.
	 * @return Un ResponseEntity con un GeneralResponseDto indicando el resultado de
	 *         la operación.
	 * @throws UserNotFoundException   Si no se encuentra el usuario especificado.
	 * @throws TallerNotFoundException Si no se encuentra el taller especificado.
	 * @throws Exception               Excepciones genéricas que puedan surgir
	 *                                 durante el procesamiento.
	 */
	ResponseEntity<GeneralResponseDto> signOutTaller(Long tallerId, Long userId, HttpServletRequest serverRequest)
			throws UserNotFoundException, TallerNotFoundException, Exception;

	/**
	 * Añade la pareja de un usuario a un taller.
	 *
	 * @param tallerUserId   el ID del usuario en el taller
	 * @param userId
	 * @param parejaEmail    el email de la pareja
	 * @param servletRequest la solicitud HTTP
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 * @throws UserNotFoundException   si el usuario no es encontrado
	 * @throws TallerNotFoundException si el taller no es encontrado
	 * @throws Exception               si ocurre un error general
	 */
	ResponseEntity<GeneralResponseDto> addPartnerTaller(Long tallerId, Long userId, String parejaEmail,
			HttpServletRequest servletRequest) throws UserNotFoundException, TallerNotFoundException, Exception;

	/**
	 * Actualiza la información de un taller existente.
	 *
	 * @param tallerDto la información actualizada del taller
	 * @param tallerId  el ID del taller a actualizar
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	ResponseEntity<BasicResponseDto> updateTaller(TallerDto tallerDto, Long tallerId);

	/**
	 * Elimina un taller existente.
	 *
	 * @param tallerId el ID del taller a eliminar
	 * @return ResponseEntity con el resultado de la operación y su estado
	 *         correspondiente
	 */
	ResponseEntity<GeneralResponseDto> deleteTaller(Long tallerId);

	/**
	 * Obtiene una lista de todos los talleres disponibles.
	 *
	 * @return Un ResponseEntity con una lista de TallerDto.
	 */
	ResponseEntity<List<TallerDto>> getListTalleres();

	/**
	 * Obtiene un taller específico por su ID.
	 *
	 * @param tallerId El ID del taller a buscar.
	 * @return Un ResponseEntity con el Taller encontrado.
	 * @throws TallerNotFoundException Si no se encuentra ningún taller con el ID
	 *                                 proporcionado.
	 */
	ResponseEntity<Taller> getTallerById(Long tallerId) throws TallerNotFoundException;

	/**
	 * Verifica si un usuario está registrado en un taller específico.
	 *
	 * @param tallerId El ID del taller.
	 * @param userId   El ID del usuario.
	 * @return true si el usuario está registrado en el taller, false en caso
	 *         contrario.
	 * @throws Exception Excepciones genéricas que puedan surgir durante el
	 *                   procesamiento.
	 */
	boolean isUserSignedUp(Long tallerId, Long userId) throws Exception;

	/**
	 * Verifica si un usuario tiene un socio en un taller específico.
	 *
	 * @param tallerId El ID del taller.
	 * @param userId   El ID del usuario.
	 * @return true si el usuario tiene un socio en el taller, false en caso
	 *         contrario.
	 */
	boolean isUserHasPartner(Long tallerId, Long userId);

	/**
	 * Empareja a un usuario con otro usuario en un taller.
	 * 
	 * @param userTaller    Relación entre usuario y taller que se va a emparejar.
	 * @param tallerId      ID del taller en el que se realizará el emparejamiento.
	 * @param serverRequest HttpServletRequest utilizado para construir la URL de la
	 *                      aplicación.
	 * @return ResponseEntity que envía una respuesta con un objeto
	 *         GeneralResponseDto.
	 */
	GeneralResponseDto emparejarUsuario(User_Taller userTaller, Long tallerId, HttpServletRequest serverRequest);

	/**
	 * Maneja las actualizaciones y notificaciones relacionadas con la pareja de un
	 * usuario que se ha dado de baja de un "Taller".
	 * 
	 * Este método actualiza el estado de la pareja del usuario tras su baja. Si la
	 * pareja ya no tiene compañero, se publica un evento para notificarle. Si la
	 * pareja es emparejada con un nuevo usuario, se envía una notificación
	 * informando sobre el nuevo emparejamiento.
	 *
	 * @param userTaller    La entidad {@link User_Taller} que representa al usuario
	 *                      que se ha dado de baja.
	 * @param tallerId      El ID del {@link Taller} del que el usuario se ha dado
	 *                      de baja.
	 * @param url           La URL base de la aplicación, utilizada en las
	 *                      notificaciones.
	 * @param serverRequest La solicitud {@link HttpServletRequest} que proporciona
	 *                      los detalles de la petición actual.
	 * 
	 * @throws Exception Si ocurre un error al buscar a la pareja, al actualizar su
	 *                   estado o al enviar las notificaciones.
	 */
	void handlePartnerAfterSignOut(User_Taller userTaller, Long tallerId, String url, HttpServletRequest serverRequest)
			throws Exception;

	/**
	 * Actualiza el estado del taller del compañero después de que un usuario se
	 * haya dado de baja del taller. Si el usuario dado de baja es un "líder", se
	 * establece el estado del compañero como "FOLLOWERWAITING" (esperando
	 * seguidor), de lo contrario, se establece como "LIDERWAITING" (esperando
	 * líder). Además, se elimina la referencia a la pareja del compañero.
	 *
	 * @param userTaller    el objeto que representa al usuario que se ha dado de
	 *                      baja del taller
	 * @param partnerTaller el objeto que representa al compañero del usuario dado
	 *                      de baja
	 */
	void updatePartnerStateAfterSignOut(User_Taller userTaller, User_Taller partnerTaller);

	/**
	 * Notifica a un usuario sobre su nueva pareja en el taller después de que su
	 * pareja anterior haya firmado la baja. El método busca al nuevo compañero de
	 * baile utilizando el ID del compañero actual, lanza una excepción si no se
	 * encuentra, y luego publica un evento para notificar al usuario original sobre
	 * su nueva pareja.
	 *
	 * @param partnerTaller      el objeto que representa al compañero de taller del
	 *                           usuario que se dio de baja
	 * @param originalUserTaller el objeto que representa al usuario que se dio de
	 *                           baja del taller
	 * @param url                la URL de la aplicación, que puede ser utilizada en
	 *                           la notificación
	 * @throws UserNotFoundException si no se encuentra el nuevo compañero de taller
	 *                               o el usuario asociado
	 */
	void notifyNewPartner(User_Taller partnerTaller, User_Taller originalUserTaller, String url);

}
