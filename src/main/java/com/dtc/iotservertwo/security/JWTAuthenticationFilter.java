package com.dtc.iotservertwo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.dtc.iotservertwo.model.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.dtc.iotservertwo.security.SecurityConstants.EXPIRATION_TIME;
import static com.dtc.iotservertwo.security.SecurityConstants.HEADER_STRING;
import static com.dtc.iotservertwo.security.SecurityConstants.SECRET;
import static com.dtc.iotservertwo.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			ApplicationUser creds = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(),
							creds.getPassword(), 
							new ArrayList<>()
							)
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {        
		ApplicationUser authUser = new ApplicationUser();
		Date expirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
		String token = JWT.create()
				.withSubject(((User) authResult.getPrincipal()).getUsername())
				.withExpiresAt(expirationTime)
				.sign(HMAC512(SECRET.getBytes()));
		authUser.setUsername(((User) authResult.getPrincipal()).getUsername());
		authUser.setToken(token);
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(authUser));
	}
	
	
}
