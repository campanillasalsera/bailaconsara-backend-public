package com.bailaconsarabackend.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bailaconsarabackend.model.Token;
import com.bailaconsarabackend.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio que proporciona funcionalidades para la generación, validación y
 * administración de tokens JWT. Se utiliza para gestionar la autenticación y
 * seguridad en el sistema mediante el uso de tokens seguros.
 */
@Service
public class JwtService {

	private final TokenRepository tokenRepository;

	/**
	 * Constructor para la clase JwtService.
	 *
	 * @param tokenRepository Repositorio de tokens utilizado para almacenar y
	 *                        recuperar tokens JWT.
	 */
	public JwtService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Value("${jwt.secret.key}")
	private String secreKey;

	@Value("${jwt.time.expiration}")
	private String timeExpiration;

	/**
	 * Genera un token JWT para el usuario proporcionado sin pasarle claims
	 * adicionales.
	 *
	 * @param userDetails Los detalles del usuario para el cual se generará el
	 *                    token.
	 * @return Un token JWT codificado.
	 */
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * Genera un token JWT para el usuario proporcionado con claims adicionales.
	 *
	 * @param extracClaims Un mapa de reivindicaciones adicionales a incluir en el
	 *                     token.
	 * @param userDetails  Los detalles del usuario para el cual se generará el
	 *                     token.
	 * @return Un token JWT codificado.
	 */
	public String generateToken(Map<String, Object> extracClaims, UserDetails userDetails) {
		return Jwts.builder().claims(extracClaims).subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
				.signWith(getSingningKey()).compact();
	}

	/**
	 * Extrae el nombre de usuario del token JWT.
	 *
	 * @param token El token JWT del cual se extraerá el nombre de usuario.
	 * @return El nombre de usuario extraído del token JWT.
	 */
	public String getUserName(String token) {
		return getClaim(token, Claims::getSubject);
	}

	/**
	 * Usa la Clase Claims y el método que definimos después getAllclaims. Valida el
	 * token JWT y verifica si el nombre de usuario extraído del token coincide con
	 * el nombre de usuario del objeto UserDetails proporcionado y si el token no ha
	 * expirado.
	 *
	 * @param <T>            El tipo de dato del valor de la reclamación.
	 * @param token          El token JWT del cual se extraerá la reclamación.
	 * @param claimsResolver Una función que define cómo se debe procesar el objeto
	 *                       Claims para obtener el valor deseado.
	 * @return El resultado de aplicar la función claimsResolver al objeto Claims,
	 *         que puede ser cualquier tipo de dato dependiendo de la función
	 *         proporcionada.
	 */
	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Obtiene todas las reivindicaciones (claims) del token JWT.
	 *
	 * @param token El token JWT del cual se extraerán las reivindicaciones.
	 * @return Un objeto Claims que contiene todas las reivindicaciones del token
	 *         JWT.
	 */
	public Claims getAllClaims(String token) {
		return Jwts.parser().verifyWith(getSingningKey()).build().parseSignedClaims(token).getPayload();
	}

	/**
	 * Genera una clave de firma a partir de la clave secreta codificada en base64.
	 *
	 * @return Una clave de firma HMAC SHA.
	 */
	public SecretKey getSingningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secreKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Valida el token JWT y verifica si el nombre de usuario extraído del token
	 * coincide con el nombre de usuario del objeto UserDetails proporcionado y si
	 * el token no ha expirado.
	 *
	 * @param token       El token JWT a validar.
	 * @param userDetails Los detalles del usuario para verificar.
	 * @return true si el token es válido, false en caso contrario.
	 */
	public boolean validateToken(String stringToken, UserDetails userDetails)
			throws ExpiredJwtException, NoSuchElementException {
		Optional<Token> tokenOptional = tokenRepository.findByToken(stringToken);
		if (!tokenOptional.isPresent()) {
			throw new NoSuchElementException("Jwtservice.validateToken = el token no existe");
		}
		Token token = tokenOptional.get();
		final String username = getUserName(stringToken);
		boolean isValidToken = tokenRepository.findByToken(stringToken) != null && !token.isLoggedout();
		if (isTokenExpired(stringToken)) {
			throw new ExpiredJwtException(null, null, "El token ha expirado");
		}

		return (username.equals(userDetails.getUsername()) && !isTokenExpired(stringToken) && isValidToken);
	}

	/**
	 * Verifica si el token JWT ha expirado.
	 *
	 * @param token El token JWT a verificar.
	 * @return true si el token ha expirado, false en caso contrario.
	 */
	public boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date());
	}

	/**
	 * Obtiene la fecha de expiración del token JWT.
	 *
	 * @param token El token JWT del cual se obtendrá la fecha de expiración.
	 * @return La fecha de expiración del token JWT.
	 */
	private Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}
}
