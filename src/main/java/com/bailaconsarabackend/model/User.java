package com.bailaconsarabackend.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad para almacenar detalles de usuarios.
 */
@Entity
@Table(name = "usuarios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private String apellidos;

	private LocalDate fechanacimiento;

	private String telefono;

	@NaturalId(mutable = true)
	private String email;

	private String password;

	/*
	 * Rol del usuario dentro del baile, puede ser lider o follower
	 */
	private String bailerol;

	/*
	 * Role del usuario. Puede ser USUARIO o ADMINISTRADOR
	 */
	@Enumerated(EnumType.ORDINAL)
	private Role role;

	/*
	 * Maneja que la cuenta del usuario esté o no habilitada
	 */
	private boolean isEnabled = false;

	/*
	 * Maneja que la cuenta del usuario esté o no bloqueada
	 */
	private boolean isNotLocked = false;

	/*
	 * Token único para la sesión del usuario
	 */
	@OneToOne(mappedBy = "user")
	private Token token;

	/*
	 * Entidad que se usa cuando a un usuario se le ha olvidado la contraseña Guarda
	 * la OTP enviada y la verificación de la misma
	 */
	@OneToOne(mappedBy = "user")
	private ForgotPassword forgotPassword;

	/*
	 * Lista de usuarios de talleres Relación con la clase intermedia que relaciona
	 * usuarios con talleres.
	 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User_Taller> usuarios_talleres;

	/**
	 * Constructor predeterminado.
	 */
	public User() {

	}

	/**
	 * Constructor para inicializar todos los campos.
	 *
	 * @param id                Identificador único.
	 * @param nombre            Nombre del usuario.
	 * @param apellidos         Apellidos del usuario.
	 * @param fechanacimiento   Fecha de nacimiento del usuario.
	 * @param telefono          Número de teléfono del usuario.
	 * @param email             Dirección de correo electrónico del usuario.
	 * @param password          Contraseña del usuario.
	 * @param bailerol          Rol del usuario.
	 * @param role              Rol del usuario.
	 * @param isEnabled         Indica si el usuario está habilitado.
	 * @param isNotLocked       Indica si el usuario no está bloqueado.
	 * @param token             Token asociado al usuario.
	 * @param forgotPassword    Detalles de restablecimiento de contraseña asociados
	 *                          al usuario.
	 * @param usuarios_talleres Workshops a los que asistirá el usuario
	 */
	public User(Long id, String nombre, String apellidos, LocalDate fechanacimiento, String telefono, String email,
			String password, String bailerol, Role role, boolean isEnabled, boolean isNotLocked, Token token,
			ForgotPassword forgotPassword, List<User_Taller> usuarios_talleres) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechanacimiento = fechanacimiento;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
		this.bailerol = bailerol;
		this.role = role;
		this.isEnabled = isEnabled;
		this.isNotLocked = isNotLocked;
		this.token = token;
		this.forgotPassword = forgotPassword;
		this.usuarios_talleres = usuarios_talleres;
	}

	/**
	 * Getters y Setters
	 */

	public List<User_Taller> getUsuarios_talleres() {
		return usuarios_talleres;
	}

	public void setUsuarios_talleres(List<User_Taller> usuarios_talleres) {
		this.usuarios_talleres = usuarios_talleres;
	}

	public ForgotPassword getForgotPassword() {
		return forgotPassword;
	}

	public void setForgotPassword(ForgotPassword forgotPassword) {
		this.forgotPassword = forgotPassword;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public LocalDate getFechanacimiento() {
		return fechanacimiento;
	}

	public void setFechanacimiento(LocalDate fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBailerol() {
		return bailerol;
	}

	public void setBailerol(String bailerol) {
		this.bailerol = bailerol;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isNotLocked() {
		return isNotLocked;
	}

	public void setNotLocked(boolean isNotLocked) {
		this.isNotLocked = isNotLocked;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * Devuelve las autoridades otorgadas al usuario. Las autoridades representan
	 * los roles o permisos que tiene el usuario.
	 *
	 * @return una colección de autoridades otorgadas al usuario.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	/**
	 * Devuelve el nombre de usuario utilizado para autenticar al usuario. Este
	 * nombre de usuario se utiliza para identificar al usuario en el sistema.
	 *
	 * @return el nombre de usuario del usuario.
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/**
	 * Indica si la cuenta del usuario ha expirado. Una cuenta expirada no puede ser
	 * autenticada.
	 *
	 * @return true si la cuenta del usuario es válida (es decir, no ha expirado),
	 *         false si ya no es válida (es decir, ha expirado).
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indica si el usuario está bloqueado o desbloqueado. Un usuario bloqueado no
	 * puede ser autenticado.
	 *
	 * @return true si el usuario no está bloqueado, false en caso contrario.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return isNotLocked;
	}

	/**
	 * Indica si las credenciales del usuario (contraseña) han expirado.
	 * Credenciales expiradas impiden la autenticación.
	 *
	 * @return true si las credenciales del usuario son válidas (es decir, no han
	 *         expirado), false si ya no son válidas (es decir, han expirado).
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indica si el usuario está habilitado o deshabilitado. Un usuario
	 * deshabilitado no puede ser autenticado.
	 *
	 * @return true si el usuario está habilitado, false en caso contrario.
	 */
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
