package com.bailaconsarabackend.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bailaconsarabackend.dto.BasicResponseDto;
import com.bailaconsarabackend.dto.GeneralResponseDto;
import com.bailaconsarabackend.dto.TallerDto;
import com.bailaconsarabackend.dto.UserTallerDto;
import com.bailaconsarabackend.event.NotificarNuevaParejaEvent;
import com.bailaconsarabackend.event.NotificarParejaEvent;
import com.bailaconsarabackend.event.NotificarSinParejaEvent;
import com.bailaconsarabackend.event.NotificarUpdatedEvent;
import com.bailaconsarabackend.exception.TallerNotFoundException;
import com.bailaconsarabackend.exception.UserAlreadyExistsException;
import com.bailaconsarabackend.exception.UserNotFoundException;
import com.bailaconsarabackend.model.Taller;
import com.bailaconsarabackend.model.User;
import com.bailaconsarabackend.model.UserTallerEstado;
import com.bailaconsarabackend.model.User_Taller;
import com.bailaconsarabackend.repository.TallerRepository;
import com.bailaconsarabackend.repository.UserRepository;
import com.bailaconsarabackend.repository.User_TallerRepository;
import com.bailaconsarabackend.service.TallerService;
import com.bailaconsarabackend.util.ApplicationUrlUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * Implementación de la interfaz TallerService para gestionar operaciones
 * relacionadas con talleres y usuarios en ellos. Incluye métodos para el
 * registro de usuarios, gestión de parejas, actualización y eliminación de
 * talleres.
 */
@Service
public class TallerServiceImpl implements TallerService {

	private final UserRepository userRepository;
	private final TallerRepository tallerRepository;
	private final User_TallerRepository user_tallerRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final ApplicationUrlUtil applicationUrlUtil;

	/**
	 * Constructor de la clase TallerServiceImpl.
	 * 
	 * @param userRepository        Repositorio de usuarios.
	 * @param tallerRepository      Repositorio de talleres.
	 * @param user_tallerRepository Repositorio de relaciones entre usuarios y
	 *                              talleres.
	 * @param eventPublisher        Publicador de eventos de la aplicación.
	 * @param applicationUrlUtil    Utilidad para obtener la URL de la aplicación.
	 */
	public TallerServiceImpl(UserRepository userRepository, TallerRepository tallerRepository,
			User_TallerRepository user_tallerRepository, ApplicationEventPublisher eventPublisher,
			ApplicationUrlUtil applicationUrlUtil) {
		this.userRepository = userRepository;
		this.tallerRepository = tallerRepository;
		this.user_tallerRepository = user_tallerRepository;
		this.eventPublisher = eventPublisher;
		this.applicationUrlUtil = applicationUrlUtil;
	}

	/**
	 * Agrega un nuevo taller.
	 * 
	 * @param tallerDto Objeto TallerDto que contiene los detalles del nuevo taller.
	 * @return ResponseEntity que envía una respuesta con un objeto
	 *         GeneralResponseDto.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> addTaller(TallerDto tallerDto) {
		GeneralResponseDto response = new GeneralResponseDto();

		try {
			Taller taller = new Taller();
			taller.setNombre(tallerDto.getNombre());
			taller.setModalidad(tallerDto.getModalidad());
			taller.setProfesores(tallerDto.getProfesores());
			taller.setFecha(tallerDto.getFecha());
			taller.setHora(tallerDto.getHora());
			taller.setLugar(tallerDto.getLugar());
			tallerRepository.save(taller);

			response.setMessage("Evento creado con éxito");
			response.setStatus(HttpStatus.CREATED);
		} catch (Exception e) {
			response.setMessage("¡Error, no se ha podido crear el evento");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Actualiza los detalles de un taller existente.
	 * 
	 * @param request  Objeto TallerDto que contiene los detalles actualizados del
	 *                 taller.
	 * @param tallerId ID del taller que se va a actualizar.
	 * @return ResponseEntity que envía una respuesta con un objeto TallerDto.
	 */
	@Override
	public ResponseEntity<BasicResponseDto> updateTaller(TallerDto request, Long tallerId) {
		Taller taller = tallerRepository.getReferenceById(tallerId);
		ArrayList<String> modificaciones = new ArrayList<>();
		String tallerNombre = taller.getNombre();
		BasicResponseDto response = new BasicResponseDto();

		try {
			if (!Objects.equals(taller.getNombre(), request.getNombre()) && request.getNombre() != null
					&& !request.getNombre().isBlank()) {
				taller.setNombre(request.getNombre());
				modificaciones.add("Se ha modificado el Nombre: " + taller.getNombre());
			}
			if (!Objects.equals(taller.getModalidad(), request.getModalidad()) && request.getModalidad() != null
					&& !request.getModalidad().isBlank()) {
				taller.setModalidad(request.getModalidad());
				modificaciones.add("Se ha modificado la Modalidad: " + taller.getModalidad());
			}

			if (!Objects.equals(taller.getProfesores(), request.getProfesores()) && request.getProfesores() != null
					&& !request.getProfesores().isBlank()) {
				taller.setProfesores(request.getProfesores());
				modificaciones.add("Se han modificado los Profesores: " + taller.getProfesores());
			}

			if (!Objects.equals(taller.getLugar(), request.getLugar()) && request.getLugar() != null
					&& !request.getLugar().isBlank()) {
				taller.setLugar(request.getLugar());
				modificaciones.add("Se ha modificado la Ubicación: " + taller.getLugar());
			}

			if (!Objects.equals(taller.getFecha(), request.getFecha()) && request.getFecha() != null) {
				taller.setFecha(request.getFecha());
				modificaciones.add("Se ha modificado la Fecha: " + taller.getFecha());
			}

			if (!Objects.equals(taller.getHora(), request.getHora()) && request.getHora() != null) {
				taller.setHora(request.getHora());
				modificaciones.add("Se ha modificado la Hora: " + taller.getHora());
			}

			tallerRepository.save(taller);
			TallerDto tallerDto = new TallerDto(taller);

			List<User_Taller> usuariosTaller = taller.getUsuarios_talleres();
			if (usuariosTaller != null) {
				for (User_Taller userTaller : usuariosTaller) {
					eventPublisher.publishEvent(new NotificarUpdatedEvent(tallerNombre, userTaller, modificaciones));
				}

			}

			response.setData(tallerDto);
			response.setStatus(HttpStatus.OK);
			response.setMessage(modificaciones.toString());
		} catch (Exception e) {
			response.setData("");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("¡Error, no se ha podido actualizar el evento!");
		}

		return new ResponseEntity<BasicResponseDto>(response, response.getStatus());
	}

	/**
	 * Elimina un taller existente.
	 * 
	 * @param tallerId ID del taller que se va a eliminar.
	 * @return ResponseEntity que envía una respuesta con un objeto
	 *         GeneralResponseDto.
	 */
	@Override
	public ResponseEntity<GeneralResponseDto> deleteTaller(Long tallerId) {
		GeneralResponseDto response = new GeneralResponseDto();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Taller taller = tallerRepository.getReferenceById(tallerId);
		List<User_Taller> usuariosTaller = taller.getUsuarios_talleres();

		// Se usa para informar a los usuarios si el taller se cancela y estaba
		// programado para una fecha posterior a hoy
		String fechaFormateada = taller.getFecha().format(formatter);
		LocalDate currentDate = LocalDate.now();
		ArrayList<String> modificaciones = new ArrayList<>();
		String tallerNombre = taller.getNombre();

		try {
			// Notificar si el evento es posterior a la fecha actual y hay usuarios
			if (taller.getFecha().isAfter(currentDate) && usuariosTaller != null && !usuariosTaller.isEmpty()) {

				modificaciones.add("Se ha anulado el evento: " + tallerNombre + " con fecha " + fechaFormateada + " en "
						+ taller.getLugar());

				for (User_Taller userTaller : usuariosTaller) {
					eventPublisher.publishEvent(new NotificarUpdatedEvent(tallerNombre, userTaller, modificaciones));
				}
			}

			tallerRepository.deleteById(tallerId);
			response.setMessage("Taller eliminado con éxito.");
			response.setStatus(HttpStatus.OK);

		} catch (Exception e) {
			response.setMessage("¡Error, no se ha podido eliminar el evento");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Registra la participación de un usuario en un taller.
	 * 
	 * @param tallerId      ID del taller en el que el usuario quiere inscribirse.
	 * @param userId        ID del usuario que desea inscribirse en el taller.
	 * @param serverRequest HttpServletRequest utilizado para construir la URL de la
	 *                      aplicación.
	 * @return ResponseEntity que envía una respuesta con un objeto
	 *         GeneralResponseDto.
	 * @throws UserNotFoundException      Excepción lanzada si el usuario no se
	 *                                    encuentra en la base de datos.
	 * @throws TallerNotFoundException    Excepción lanzada si el taller no se
	 *                                    encuentra en la base de datos.
	 * @throws UserAlreadyExistsException Excepción lanzada si el usuario ya está
	 *                                    inscrito en el taller.
	 */
	@Transactional
	@Override
	public ResponseEntity<GeneralResponseDto> signInUserTaller(Long tallerId, Long userId,
			HttpServletRequest serverRequest)
			throws UserNotFoundException, TallerNotFoundException, UserAlreadyExistsException {
		GeneralResponseDto response = new GeneralResponseDto();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

		Taller taller = tallerRepository.findById(tallerId)
				.orElseThrow(() -> new TallerNotFoundException("Taller no encontrado"));

		if (user_tallerRepository.findByTallerIdAndUserId(tallerId, userId).isPresent()) {
			throw new UserAlreadyExistsException("Ya te has inscrito en este evento " + user.getNombre());
		}

		try {
			User_Taller userTaller = new User_Taller();
			userTaller.setUser(user);
			userTaller.setTaller(taller);
			String userRol = user.getBailerol();
			if (userRol.equalsIgnoreCase("lider")) {
				userTaller.setUserTallerEstado(UserTallerEstado.LIDERWAITING);
			} else {
				userTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERWAITING);
			}
			user_tallerRepository.save(userTaller);

			response = emparejarUsuario(userTaller, tallerId, serverRequest);
		} catch (Exception e) {
			response.setMessage("¡Error, no se te ha podido inscribier en el evento");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, response.getStatus());
	}

	/**
	 * Método para cancelar la inscripción de un usuario en un taller.
	 * 
	 * Este método gestiona la lógica para anular la inscripción de un usuario en un
	 * taller. Si el usuario tiene una pareja asignada, también se actualiza el
	 * estado de la pareja y se intenta encontrar una nueva pareja si es necesario.
	 *
	 * @param tallerId      ID del taller del cual el usuario desea cancelar la
	 *                      inscripción.
	 * @param userId        ID del usuario que desea cancelar su inscripción.
	 * @param serverRequest Objeto HttpServletRequest utilizado para obtener la URL
	 *                      base de la aplicación.
	 * @return ResponseEntity que contiene un GeneralResponseDto con el estado y
	 *         mensaje de la operación.
	 * @throws NotFoundException
	 * @throws Exception         si ocurre algún problema durante la cancelación de
	 *                           la inscripción o la actualización de la pareja.
	 */
	@Transactional
	@Override
	public ResponseEntity<GeneralResponseDto> signOutTaller(Long tallerId, Long userId,
			HttpServletRequest serverRequest) throws NotFoundException {
		String message = "";
		HttpStatus status;
		boolean signedOut = false;

		try {
			User_Taller userTaller = user_tallerRepository.findByTallerIdAndUserId(tallerId, userId)
					.orElseThrow(() -> new NotFoundException());

			String userName = userTaller.getUser().getNombre();
			String url = applicationUrlUtil.applicationUrl(serverRequest);

			try {
				// Elimina la inscripción del usuario
				user_tallerRepository.delete(userTaller);
				message = userName + " tu inscripción para el evento " + userTaller.getTaller().getNombre()
						+ " ha sido anulada con éxito";
				status = HttpStatus.OK;
				signedOut = true;
			} catch (Exception e) {
				message = "Ha ocurrido un error: " + e;
				status = HttpStatus.NOT_MODIFIED;
			}

			if (signedOut && userTaller.getPartnerId() != null) {

				try {
					handlePartnerAfterSignOut(userTaller, tallerId, url, serverRequest);
				} catch (NotFoundException e) {
					message += " Sin embargo, hubo un problema al actualizar la información de tu pareja: "
							+ e.getMessage();
				}
			}
		} catch (NotFoundException e) {
			message = "No se encontró el registro del usuario: " + e.getMessage();
			status = HttpStatus.NOT_FOUND;
		} catch (Exception e) {
			message = "Ocurrió un error inesperado: " + e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(new GeneralResponseDto(status, message), status);
	}

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
	@Override
	public void handlePartnerAfterSignOut(User_Taller userTaller, Long tallerId, String url,
			HttpServletRequest serverRequest) throws Exception {
		try {
			User_Taller partnerTaller = user_tallerRepository.findById(userTaller.getPartnerId())
					.orElseThrow(() -> new NotFoundException());

			updatePartnerStateAfterSignOut(userTaller, partnerTaller);

			GeneralResponseDto emparejarResult = emparejarUsuario(partnerTaller, tallerId, serverRequest);

			if (!emparejarResult.getMessage().startsWith("Enhorabuena ")) {
				eventPublisher.publishEvent(new NotificarSinParejaEvent(partnerTaller, userTaller.getUser(), url));
			} else if (partnerTaller.getPartnerId() != null) {
				try {
					notifyNewPartner(partnerTaller, userTaller, url);
				} catch (Exception e) {
					throw new Exception("Error al notificar a la nueva pareja: " + e.getMessage());
				}
			}
		} catch (NotFoundException e) {
			throw new Exception("Error al encotrar la pareja: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar el estado de la pareja: " + e.getMessage());
		}

	}

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
	@Override
	public void updatePartnerStateAfterSignOut(User_Taller userTaller, User_Taller partnerTaller) {
		if (userTaller.getUser().getBailerol().equalsIgnoreCase("lider")) {
			partnerTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERWAITING);
		} else {
			partnerTaller.setUserTallerEstado(UserTallerEstado.LIDERWAITING);
		}
		partnerTaller.setPartnerId(null);
		user_tallerRepository.save(partnerTaller);

	}

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
	@Override
	public void notifyNewPartner(User_Taller partnerTaller, User_Taller originalUserTaller, String url) {
		// Encuntra el User_Taller de la nueva pareja de partnerTaller
		User_Taller newPartnerUserTaller = user_tallerRepository.findById(partnerTaller.getPartnerId())
				.orElseThrow(() -> new UserNotFoundException("No se ha encontrado el usuario del evento. "));
		// Encuentra la nueva pareja de partnerTaller
		User newPartner = userRepository.findById(newPartnerUserTaller.getUser().getId())
				.orElseThrow(() -> new UserNotFoundException("No se ha encontrado a la nueva pareja. "));

		String newPartnerName = newPartner.getNombre() + " " + newPartner.getApellidos();
		eventPublisher.publishEvent(
				new NotificarParejaEvent(partnerTaller, originalUserTaller.getUser().getNombre(), newPartnerName, url));
	}

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
	@Override
	public GeneralResponseDto emparejarUsuario(User_Taller userTaller, Long tallerId,
			HttpServletRequest serverRequest) {
		String mensaje = "No hay resultado";
		String nombreUsuario = userTaller.getUser().getNombre();
		User_Taller parejaTaller;
		boolean emparejado = false;
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<User_Taller> usuariosTaller = user_tallerRepository.findByTallerId(tallerId);
		int i = 0;

		if (userTaller.getUserTallerEstado().name().equals("LIDERWAITING")) {
			do {
				parejaTaller = usuariosTaller.get(i);
				if (parejaTaller.getUserTallerEstado().name().equals("FOLLOWERWAITING")) {
					userTaller.setUserTallerEstado(UserTallerEstado.LIDERCONFIRMED);
					parejaTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERCONFIRMED);

					userTaller.setPartnerId(parejaTaller.getId());
					parejaTaller.setPartnerId(userTaller.getId());
					user_tallerRepository.save(userTaller);
					user_tallerRepository.save(parejaTaller);

					eventPublisher.publishEvent(new NotificarNuevaParejaEvent(parejaTaller, userTaller.getUser(),
							applicationUrlUtil.applicationUrl(serverRequest)));
					mensaje = "Enhorabuena " + nombreUsuario
							+ ", te has registrado en el evento y se te ha emparjado con "
							+ parejaTaller.getUser().getNombre() + " " + parejaTaller.getUser().getApellidos();
					emparejado = true;
					status = HttpStatus.OK;
				} else {
					mensaje = nombreUsuario
							+ ", te has registrado en el evento y quedas en lista de espera, no hay pareja disponible en este momento . Te avisaremos cuando el sistema te consiga una pareja. También puedes añadir tu propia pareja.";
					status = HttpStatus.OK;
				}
				i++;
			} while (!emparejado && i < usuariosTaller.size());

		} else if (userTaller.getUserTallerEstado().name().equals("FOLLOWERWAITING")) {
			do {
				parejaTaller = usuariosTaller.get(i);
				if (parejaTaller.getUserTallerEstado().name().equals("LIDERWAITING")) {
					userTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERCONFIRMED);
					parejaTaller.setUserTallerEstado(UserTallerEstado.LIDERCONFIRMED);

					userTaller.setPartnerId(parejaTaller.getId());
					parejaTaller.setPartnerId(userTaller.getId());
					user_tallerRepository.save(userTaller);
					user_tallerRepository.save(parejaTaller);

					eventPublisher.publishEvent(new NotificarNuevaParejaEvent(parejaTaller, userTaller.getUser(),
							applicationUrlUtil.applicationUrl(serverRequest)));
					mensaje = "Enhorabuena " + nombreUsuario
							+ ", te has registrado en el evento y se te ha emparjado con "
							+ parejaTaller.getUser().getNombre() + " " + parejaTaller.getUser().getApellidos();
					emparejado = true;
					status = HttpStatus.OK;
				} else {
					mensaje = nombreUsuario
							+ " te has registrado en el evento y quedas en lista de espera, no hay pareja disponible aun . Te avisaremos cuando el sistema te consiga una pareja.";
					status = HttpStatus.OK;
				}
				i++;
			} while (!emparejado && i < usuariosTaller.size());

		}

		return new GeneralResponseDto(status, mensaje);
	}

	/**
	 * Obtiene la lista de usuarios inscritos en un taller.
	 * 
	 * @param tallerId ID del taller del que se desea obtener la lista de usuarios.
	 * @return ResponseEntity que envía una respuesta con una lista de objetos User.
	 */
	@Override
	public ResponseEntity<List<UserTallerDto>> getUsuariosByTallerId(Long tallerId) {
		List<User_Taller> usersTaller = user_tallerRepository.findByTallerId(tallerId);
		List<UserTallerDto> usuariosTallerDto = new ArrayList<>();

		for (User_Taller userTaller : usersTaller) {
			Optional<User_Taller> partnerOptional = Optional.of(new User_Taller());
			String nombrePartner = "";

			UserTallerDto userTallerDto = new UserTallerDto();
			userTallerDto.setId(userTaller.getId());
			userTallerDto.setNombre(userTaller.getUser().getNombre());
			userTallerDto.setApellidos(userTaller.getUser().getApellidos());
			userTallerDto.setEmail(userTaller.getUser().getEmail());
			userTallerDto.setTelefono(userTaller.getUser().getTelefono());
			userTallerDto.setBailerol(userTaller.getUser().getBailerol());
			userTallerDto.setUser_taller_estado(userTaller.getUserTallerEstado());
			if (userTaller.getPartnerId() != null) {
				User userPartner = new User();
				partnerOptional = user_tallerRepository.findById(userTaller.getPartnerId());
				userPartner = partnerOptional.get().getUser();
				nombrePartner = userPartner.getNombre() + " " + userPartner.getApellidos();
				userTallerDto.setUserPartner(nombrePartner);
			}

			usuariosTallerDto.add(userTallerDto);

		}
		return new ResponseEntity<>(usuariosTallerDto, HttpStatus.OK);
	}

	/**
	 * Registra la participación de una pareja en un taller.
	 * 
	 * @param tallerId       ID del taller en el que la pareja quiere inscribirse.
	 * @param userId         ID del usuario que desea inscribirse en el taller como
	 *                       parte de la pareja.
	 * @param parejaEmail    Correo electrónico de la pareja que se va a inscribir
	 *                       en el taller.
	 * @param servletRequest HttpServletRequest utilizado para construir la URL de
	 *                       la aplicación.
	 * @return ResponseEntity que envía una respuesta con un objeto
	 *         GeneralResponseDto.
	 * @throws UserNotFoundException      Excepción lanzada si el usuario no se
	 *                                    encuentra en la base de datos.
	 * @throws TallerNotFoundException    Excepción lanzada si el taller no se
	 *                                    encuentra en la base de datos.
	 * @throws UserAlreadyExistsException Excepción lanzada si el usuario ya está
	 *                                    inscrito en el taller.
	 */
	@Transactional
	@Override
	public ResponseEntity<GeneralResponseDto> signInParejaTaller(Long tallerId, Long userId, String parejaEmail,
			HttpServletRequest servletRequest)
			throws UserNotFoundException, TallerNotFoundException, UserAlreadyExistsException {
		String mensaje = "";
		HttpStatus status;

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

		User pareja = userRepository.findByEmail(parejaEmail).orElseThrow(
				() -> new UserNotFoundException("No se ha encontrado el usuario con el email: " + parejaEmail));

		Taller taller = tallerRepository.findById(tallerId)
				.orElseThrow(() -> new TallerNotFoundException("Taller no encontrado"));

		if (user_tallerRepository.findByTallerIdAndUserId(tallerId, userId).isPresent()) {
			throw new UserAlreadyExistsException("Ya te has inscrito en este evento " + user);
		}
		if (user_tallerRepository.findByTallerIdAndUserId(tallerId, pareja.getId()).isPresent()) {
			throw new UserAlreadyExistsException(
					"Tu pareja " + pareja.getNombre() + " ya se ha inscrito en este evento.");
		}

		String userRol = user.getBailerol();
		String parejaRol = pareja.getBailerol();

		User_Taller userTaller = new User_Taller();
		User_Taller parejaTaller = new User_Taller();

		String url = applicationUrlUtil.applicationUrl(servletRequest);

		if (userRol.equalsIgnoreCase("lider") && parejaRol.equalsIgnoreCase("follower")) {
			userTaller.setUser(user);
			userTaller.setTaller(taller);
			userTaller.setUserTallerEstado(UserTallerEstado.LIDERCONFIRMED);
			user_tallerRepository.save(userTaller);

			parejaTaller.setUser(pareja);
			parejaTaller.setTaller(taller);
			parejaTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERCONFIRMED);
			user_tallerRepository.save(parejaTaller);

			parejaTaller.setPartnerId(userTaller.getId());
			userTaller.setPartnerId(parejaTaller.getId());
			user_tallerRepository.save(userTaller);
			user_tallerRepository.save(parejaTaller);

			mensaje = "Pareja inscrita con éxito en el evento";
			status = HttpStatus.OK;
		} else if (userRol.equalsIgnoreCase("follower") && parejaRol.equalsIgnoreCase("lider")) {
			userTaller.setUser(user);
			userTaller.setTaller(taller);
			userTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERCONFIRMED);
			user_tallerRepository.save(userTaller);

			parejaTaller.setUser(pareja);
			parejaTaller.setTaller(taller);
			parejaTaller.setUserTallerEstado(UserTallerEstado.LIDERCONFIRMED);
			user_tallerRepository.save(parejaTaller);

			parejaTaller.setPartnerId(userTaller.getId());
			userTaller.setPartnerId(parejaTaller.getId());
			user_tallerRepository.save(userTaller);
			user_tallerRepository.save(parejaTaller);

			mensaje = "Pareja inscrita con éxito en el evento";
			status = HttpStatus.OK;
		} else {
			mensaje = "Para inscribirse como pareja en el evento es necesario que uno de los dos sea LIDER y el otro FOLLOWER";
			status = HttpStatus.BAD_REQUEST;
		}

		if (status.is2xxSuccessful()) {
			eventPublisher.publishEvent(new NotificarNuevaParejaEvent(parejaTaller, userTaller.getUser(), url));
		}
		return new ResponseEntity<>(new GeneralResponseDto(status, mensaje), status);
	}

	/**
	 * Método para añadir una pareja para un usuario inscrito a un taller.
	 * 
	 * Este método permite que un usuario inscriba a su pareja en un taller, siempre
	 * y cuando cumplan con las condiciones necesarias (uno debe ser LIDER y el otro
	 * FOLLOWER). También envía notificaciones si la inscripción es exitosa.
	 *
	 * @param tallerId       ID del taller al cual se inscribirá la pareja.
	 * @param userId         ID del usuario que desea inscribir a su pareja.
	 * @param parejaEmail    Email de la pareja que se desea inscribir.
	 * @param servletRequest Objeto HttpServletRequest utilizado para obtener la URL
	 *                       base de la aplicación.
	 * @return ResponseEntity que contiene un GeneralResponseDto con el estado y
	 *         mensaje de la operación.
	 * @throws UserNotFoundException   si el usuario con el email proporcionado no
	 *                                 es encontrado.
	 * @throws TallerNotFoundException si el taller no es encontrado.
	 * @throws Exception               si ocurre algún problema durante la
	 *                                 inscripción.
	 */
	@Transactional
	@Override
	public ResponseEntity<GeneralResponseDto> addPartnerTaller(Long tallerId, Long userId, String parejaEmail,
			HttpServletRequest servletRequest) throws UserNotFoundException, TallerNotFoundException, Exception {
		String mensaje = "";
		HttpStatus status;

		User_Taller userTaller = user_tallerRepository.findByTallerIdAndUserId(tallerId, userId)
				.orElseThrow(() -> new Exception("No hay registro de este usuraio en este taller"));

		User pareja = userRepository.findByEmail(parejaEmail).orElseThrow(
				() -> new UserNotFoundException("No se ha encontrado el usuario con el email: " + parejaEmail));

		Taller taller = userTaller.getTaller();

		if (user_tallerRepository.findByTallerIdAndUserId(tallerId, pareja.getId()).isPresent()) {
			throw new UserAlreadyExistsException(
					"Tu pareja " + pareja.getNombre() + " ya se ha inscrito en este evento.");
		}

		String parejaRol = pareja.getBailerol();
		User_Taller parejaTaller = new User_Taller();
		String url = applicationUrlUtil.applicationUrl(servletRequest);

		if (userTaller.getUserTallerEstado().name().equals("LIDERWAITING") && parejaRol.equalsIgnoreCase("follower")) {
			userTaller.setUserTallerEstado(UserTallerEstado.LIDERCONFIRMED);

			parejaTaller.setUser(pareja);
			parejaTaller.setTaller(taller);
			parejaTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERCONFIRMED);
			parejaTaller.setPartnerId(userTaller.getId());
			user_tallerRepository.save(parejaTaller);

			userTaller.setPartnerId(parejaTaller.getId());
			user_tallerRepository.save(userTaller);

			mensaje = "Pareja inscrita con éxito en el evento";
			status = HttpStatus.OK;

		} else if (userTaller.getUserTallerEstado().name().equals("FOLLOWERWAITING")
				&& parejaRol.equalsIgnoreCase("lider")) {
			userTaller.setUserTallerEstado(UserTallerEstado.FOLLOWERCONFIRMED);

			parejaTaller.setUser(pareja);
			parejaTaller.setTaller(taller);
			parejaTaller.setUserTallerEstado(UserTallerEstado.LIDERCONFIRMED);
			parejaTaller.setPartnerId(userTaller.getId());
			user_tallerRepository.save(parejaTaller);

			userTaller.setPartnerId(parejaTaller.getId());
			user_tallerRepository.save(userTaller);

			mensaje = "Pareja inscrita con éxito en el evento";
			status = HttpStatus.OK;
		} else {
			mensaje = "Para inscribirse como pareja en el evento es necesario que uno de los dos sea LIDER y el otro FOLLOWER";
			status = HttpStatus.BAD_REQUEST;
		}

		if (status.is2xxSuccessful()) {
			eventPublisher.publishEvent(new NotificarNuevaParejaEvent(parejaTaller, userTaller.getUser(), url));
		}
		return new ResponseEntity<>(new GeneralResponseDto(status, mensaje), status);
	}

	/**
	 * Obtiene una lista de todos los talleres disponibles.
	 * 
	 * Este método recupera todos los talleres almacenados en la base de datos y los
	 * convierte en objetos de tipo TallerDto para ser enviados como respuesta.
	 *
	 * @return ResponseEntity que contiene una lista de TallerDto y el estado HTTP
	 *         correspondiente.
	 */
	@Override
	public ResponseEntity<List<TallerDto>> getListTalleres() {
		List<Taller> talleres = tallerRepository.findAll();
		List<TallerDto> talleresDto = new ArrayList<>();
		for (Taller taller : talleres) {
			TallerDto tallerDto = new TallerDto(taller);
			talleresDto.add(tallerDto);
		}
		return new ResponseEntity<>(talleresDto, HttpStatus.OK);
	}

	/**
	 * Obtiene un taller por su ID.
	 * 
	 * Este método busca un taller en la base de datos por su ID y lo devuelve si se
	 * encuentra. Si no se encuentra el taller, se lanza una excepción
	 * TallerNotFoundException.
	 *
	 * @param tallerId ID del taller a buscar.
	 * @return ResponseEntity que contiene el taller encontrado y el estado HTTP
	 *         correspondiente.
	 * @throws TallerNotFoundException si no se encuentra un taller con el ID
	 *                                 proporcionado.
	 */
	@Override
	public ResponseEntity<Taller> getTallerById(Long tallerId) throws TallerNotFoundException {
		Taller taller = tallerRepository.findById(tallerId)
				.orElseThrow(() -> new TallerNotFoundException("Taller no encontrado"));
		return new ResponseEntity<>(taller, HttpStatus.OK);
	}

	/**
	 * Verifica si un usuario está inscrito en un taller.
	 * 
	 * Este método comprueba si un usuario específico está inscrito en un taller
	 * determinado mediante la búsqueda en la tabla User_Taller.
	 *
	 * @param tallerId ID del taller.
	 * @param userId   ID del usuario.
	 * @return true si el usuario está inscrito, false en caso contrario.
	 * @throws Exception si ocurre algún error durante la búsqueda.
	 */
	@Override
	public boolean isUserSignedUp(Long tallerId, Long userId) throws Exception {
		boolean isUserSuscribed = false;
		Optional<User_Taller> userTaller = user_tallerRepository.findByTallerIdAndUserId(tallerId, userId);
		if (userTaller.isPresent()) {
			isUserSuscribed = true;
		}
		return isUserSuscribed;
	}

	/**
	 * Verifica si un usuario tiene pareja confirmada en un taller.
	 * 
	 * Este método comprueba si un usuario en un taller tiene una pareja confirmada,
	 * verificando el estado de su inscripción como LIDERCONFIRMED o
	 * FOLLOWERCONFIRMED.
	 *
	 * @param tallerId ID del taller.
	 * @param userId   ID del usuario.
	 * @return true si el usuario tiene una pareja confirmada, false en caso
	 *         contrario.
	 */
	@Override
	public boolean isUserHasPartner(Long tallerId, Long userId) {
		boolean isUserHasPartner = false;
		Optional<User_Taller> userTaller = user_tallerRepository.findByTallerIdAndUserId(tallerId, userId);
		if (userTaller.isPresent()) {
			User_Taller usuarioTaller = userTaller.get();
			if (usuarioTaller.getUserTallerEstado().name().equals("LIDERCONFIRMED")
					|| usuarioTaller.getUserTallerEstado().name().equals("FOLLOWERCONFIRMED")) {

				isUserHasPartner = true;
			}
		}
		return isUserHasPartner;
	}

}
