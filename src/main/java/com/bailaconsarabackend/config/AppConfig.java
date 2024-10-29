package com.bailaconsarabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bailaconsarabackend.repository.UserRepository;

/**
 * Clase de configuración de seguridad para la aplicación Bailaconsara.
 * 
 * Esta clase se encarga de configurar los beans necesarios para la
 * autenticación de usuarios, incluyendo el servicio de detalles del usuario, el
 * proveedor de autenticación y el codificador de contraseñas.
 */
@Configuration
public class AppConfig {

	private final UserRepository userRepository;

	/**
	 * Constructor de la clase AppConfig.
	 *
	 * @param userRepository el repositorio de usuarios a utilizar
	 */
	public AppConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Define un bean para el servicio de detalles del usuario. Utiliza una función
	 * lambda para proporcionar detalles del usuario a partir del nombre de usuario.
	 *
	 * @return un UserDetailsService que busca usuarios por su correo electrónico
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}

	/**
	 * Define un bean para el proveedor de autenticación.
	 *
	 * @return un AuthenticationProvider configurado con el UserDetailsService y el
	 *         PasswordEncoder
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	/**
	 * Define un bean para el codificador de contraseñas.
	 *
	 * @return un PasswordEncoder BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Define un bean para el administrador de autenticación.
	 *
	 * @param config la configuración de autenticación
	 * @return un AuthenticationManager obtenido de la configuración
	 * @throws Exception si hay un error al obtener el AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
