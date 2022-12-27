package com.tukks.cogito.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.ws.rs.NotAuthorizedException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tukks.cogito.entity.UserEntity;
import com.tukks.cogito.repository.UserRepository;
import com.tukks.cogito.utils.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import static com.tukks.cogito.utils.Constants.ACCESS_TOKEN;
import static com.tukks.cogito.utils.JwtTokenUtil.createCookieWithToken;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtTokenUtil jwtUtil;

	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		if (!hasAuthorizationBearer(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = getAccessToken(request);

		if (!jwtUtil.validateAccessToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		setAuthenticationContext(token, request, response);

		filterChain.doFilter(request, response);
	}

	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		Cookie[] header = request.getCookies();

		if (header == null || header.length == 0) {
			return false;
		}

		Optional<Cookie> accessToken = Arrays.stream(header).filter(cookie -> cookie.getName().equals(ACCESS_TOKEN)).findFirst();

		return accessToken.isPresent();
	}

	private String getAccessToken(HttpServletRequest request) {
		Cookie[] header = request.getCookies();
		Optional<Cookie> accessToken = Arrays.stream(header).filter(cookie -> cookie.getName().equals(ACCESS_TOKEN)).findFirst();
		if (accessToken.isPresent()) {
			return accessToken.get().getValue();
		}
		throw new NotAuthorizedException("Cookie not present");
	}

	private void setAuthenticationContext(String token, HttpServletRequest request, HttpServletResponse response) {
		UserDetails userDetails = getUserDetails(token);

		UsernamePasswordAuthenticationToken
			authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);

		authentication.setDetails(
			new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		createCookieWithToken(response, userDetails, jwtUtil);

	}

	private UserDetails getUserDetails(String token) {
		UserEntity userDetails;
		String[] jwtSubject = jwtUtil.getSubject(token).split(",");

		userDetails = userRepository.findByUsername(jwtSubject[0]).orElseThrow();

		return userDetails;
	}
}
