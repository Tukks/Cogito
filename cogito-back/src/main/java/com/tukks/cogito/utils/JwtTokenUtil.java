package com.tukks.cogito.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import static com.tukks.cogito.utils.Constants.ACCESS_TOKEN;

@Component
public class JwtTokenUtil {

	Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	private static final long EXPIRE_DURATION = 24 * 12 * 60 * 60 * 1000; // 12 days

	@Value("${app.jwt.secret}")
	private String SECRET_KEY;

	@Value("${app.https-only:true}")
	private Boolean HTTPS_ONLY;

	public String generateAccessToken(UserDetails user) {
		SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder()
			.setSubject(user.getUsername())
			.setIssuer("cogito")
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
			.signWith(key).compact();

	}

	public boolean validateAccessToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException ex) {
			logger.error("JWT expired: {}", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			logger.error("Token is null, empty or only whitespace, {}", ex.getMessage());
		} catch (MalformedJwtException ex) {
			logger.error("JWT is invalid", ex);
		} catch (UnsupportedJwtException ex) {
			logger.error("JWT is not supported", ex);
		} catch (SignatureException ex) {
			logger.error("Signature validation failed");
		}

		return false;
	}

	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}

	private Claims parseClaims(String token) {
		SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public void createCookieWithToken(HttpServletResponse response, UserDetails userDetails, JwtTokenUtil jwtUtil) {
		String accessToken = jwtUtil.generateAccessToken(userDetails);
		Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
		cookie.setPath("/");
		cookie.setSecure(HTTPS_ONLY);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(24 * 12 * 60 * 60);
		response.addCookie(cookie);
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}

	public void removeCookieOnLogout(HttpServletResponse response) {
		Cookie cookie = new Cookie(ACCESS_TOKEN, null);
		cookie.setPath("/");
		cookie.setSecure(HTTPS_ONLY);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}
}
