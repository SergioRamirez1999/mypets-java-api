package com.sergio.apianimals.auth.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.apianimals.auth.services.JWTService;
import com.sergio.apianimals.auth.services.JWTServiceImpl;
import com.sergio.apianimals.model.entity.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	private JWTService jwtService;
	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/apianimals/services/users/login", "POST"));
		this.jwtService = jwtService;
	}
	
	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter("email");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		if(username != null && password != null) {
			logger.info("AUTHENTICATION-FILTER-ATTEMP: USERNAME REQUEST PARAMETER (FORM-DATA): " + username);
			logger.info("AUTHENTICATION-FILTER-ATTEMP: PASSWORD REQUEST PARAMETER (FORM-DATA): " + password);
		}else {
			User user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), User.class);
				
				username = user.getEmail();
				password = user.getPassword();
				
				logger.info("AUTHENTICATION-FILTER-ATTEMP: USERNAME REQUEST INPUTSTREAM (RAW): " + username);
				logger.info("AUTHENTICATION-FILTER-ATTEMP: PASSWORD desde request InputStream (RAW): " + password);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		username = username.trim();
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		return authenticationManager.authenticate(authToken);
		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		
		String token = jwtService.create(authResult);
		
		response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", principal);
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
		logger.info("SUCCESSFUL-AUTHENTICATION: LOGIN USERNAME: " + ((org.springframework.security.core.userdetails.User)body.get("user")).getUsername());
		logger.info("SUCCESSFUL-AUTHENTICATION: LOGIN TOKEN: " + token);
		
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
		logger.error("UNSUCCESSFUL-AUTHENTICATION");
		
	}
	
}
