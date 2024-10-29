package com.bailaconsarabackend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.bailaconsarabackend.config.handler.CustomAccessDeniedHandler;
import com.bailaconsarabackend.config.handler.CustomLogoutHandler;

/**
 * Configuración de seguridad para la aplicación Bailaconsara.
 * 
 * Esta clase se encarga de configurar la seguridad de la aplicación utilizando
 * Spring Security. Se definen las reglas de autorización para diferentes
 * endpoints, el manejo de CORS y la integración del filtro JWT para la
 * autenticación de usuarios.
 *
 * La configuración incluye: - Rutas públicas y protegidas. - Manejo de errores
 * para acceso denegado y autenticación. - Filtro CORS para permitir solicitudes
 * desde orígenes específicos. - Gestión de sesiones en un modo sin estado
 * (stateless).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomLogoutHandler customLogoutHandler;

	/**
	 * Constructor de la clase SecurityConfig.
	 *
	 * @param jwtFilter                 el filtro JWT a utilizar
	 * @param authenticationProvider    el proveedor de autenticación a utilizar
	 * @param customAccessDeniedHandler el manejador de acceso denegado
	 *                                  personalizado
	 * @param customLogoutHandler       el manejador de cierre de sesión
	 *                                  personalizado
	 */
	public SecurityConfig(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider,
			CustomAccessDeniedHandler customAccessDeniedHandler, CustomLogoutHandler customLogoutHandler) {
		this.jwtFilter = jwtFilter;
		this.authenticationProvider = authenticationProvider;
		this.customAccessDeniedHandler = customAccessDeniedHandler;
		this.customLogoutHandler = customLogoutHandler;
	}

	/**
	 * Configura la cadena de filtros de seguridad.
	 *
	 * @param httpSecurity el objeto HttpSecurity a configurar
	 * @return la cadena de filtros de seguridad configurada
	 * @throws Exception si hay un error al configurar la seguridad
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable())
				// Define las rutas que van a ser publicas, todas las que estén dentro del
				// requestMatchers. permitAll
				.authorizeHttpRequests(auth -> auth.requestMatchers(publicEndpoints()).permitAll()
						// Todas las demás necesitarán autenticacion
						// Aquí solo pueden acceder los que tengan la autoridad de ADMIN
						.requestMatchers(adminEndpoints()).hasAnyAuthority("ADMIN").anyRequest().authenticated())
				// aquí manejamos que no arroje un 403 sino un 401 cuando no se tiene permiso
				.exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedHandler)
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
				.logout(l -> l.logoutUrl("/logout").addLogoutHandler(customLogoutHandler).logoutSuccessHandler(
						(request, response, authentication) -> SecurityContextHolder.clearContext()));
		return httpSecurity.build();
	}

	/**
	 * Define un RequestMatcher para las URL públicas.
	 *
	 * @return un RequestMatcher para las URL públicas
	 */
	private RequestMatcher publicEndpoints() {
		return new OrRequestMatcher(new AntPathRequestMatcher("/posts/getPost/{id}"),
				new AntPathRequestMatcher("/home"), new AntPathRequestMatcher("/auth/**"),
				new AntPathRequestMatcher("/forgotPassword/**"), new AntPathRequestMatcher("/posts/getPosts"),
				new AntPathRequestMatcher("/media/{filename:.+}"), new AntPathRequestMatcher("/salas/horarios/**"),
				new AntPathRequestMatcher("/posts/post/{title}")

		);
	}

	/**
	 * Método para crear un matcher que coincide con los endpoints de administrador.
	 * 
	 * @return Un RequestMatcher que coincide con los endpoints de administrador.
	 */
	private RequestMatcher adminEndpoints() {
		return new OrRequestMatcher(new AntPathRequestMatcher("/admin/**"), new AntPathRequestMatcher("/posts/**"),
				new AntPathRequestMatcher("/talleres/admin/**"));
	}

	/**
	 * Método para crear un matcher que coincide con los endpoints de usuario.
	 * 
	 * @return Un RequestMatcher que coincide con los endpoints de usuario.
	 */
	private RequestMatcher userEndpoints() {
		return new OrRequestMatcher(new AntPathRequestMatcher("/talleres/user/**"));
	}

	/**
	 * Configura un filtro CORS para la aplicación.
	 * 
	 * Este método crea y configura un filtro CORS que permite solicitudes de
	 * orígenes específicos y controla qué encabezados y métodos HTTP son
	 * permitidos.
	 * 
	 * @return un CorsFilter configurado con las reglas CORS definidas.
	 * 
	 *         Configuraciones: - Credenciales permitidas: Se permite el envío de
	 *         credenciales (como cookies, autenticación HTTP, etc.) a través de
	 *         CORS. - Orígenes permitidos: Se permite el acceso desde los orígenes
	 *         definidos en la lista, por ejemplo, "http://localhost:4200". -
	 *         Encabezados permitidos: Se permiten todos los encabezados en las
	 *         solicitudes CORS. - Métodos permitidos: Se permiten todos los métodos
	 *         HTTP (GET, POST, PUT, DELETE, etc.) en las solicitudes CORS. - Rutas
	 *         aplicables: Se aplican las reglas CORS a todas las rutas ("/**").
	 * 
	 *         Nota: Es importante reemplazar los orígenes permitidos por aquellos
	 *         específicos de tu aplicación en un entorno de producción.
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// Reemplaza "*"" con tus orígenes específicos permitidos
		// config.setAllowedOriginPatterns(List.of("http://localhost:4200"));
		config.setAllowedOriginPatterns(List.of("https://bailaconsara.com"));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
