package com.sergio.apianimals.security.config;

import java.util.Arrays;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sergio.apianimals.auth.filter.JwtAuthenticationFilter;
import com.sergio.apianimals.auth.filter.JwtAuthorizationFilter;
import com.sergio.apianimals.auth.services.JWTService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors()
		.and()
		.csrf()
		.disable()
		.authorizeRequests()
			.antMatchers("/apianimals/user-management/managed-users/**")
			.permitAll()
			.and()
		.authorizeRequests()
			.antMatchers("/apianimals/animal-management/managed-animals/**")
			.permitAll()
			.and()
		.authorizeRequests()
			.antMatchers("/apianimals/family-management/managed-families/**")
			.permitAll()
			.and()
		.authorizeRequests()
			.antMatchers("/apianimals/services/**")
			.permitAll()
			.and()
		.authorizeRequests()
			.antMatchers("/console/**")
			.permitAll()
			.and()
		.authorizeRequests()	
			.anyRequest()
			.authenticated()
			.and()
		.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService))
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.headers().frameOptions().disable();
	}

	
	    @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration cors = new CorsConfiguration();
	        cors.setAllowedOrigins(Arrays.asList("*"));
	        cors.setAllowedMethods(Arrays.asList("*"));
	        cors.setAllowedHeaders(Arrays.asList("*"));
	        cors.setMaxAge(1800L);
	        source.registerCorsConfiguration("/**", cors);
	        return source;
	    }
	
	
	@SuppressWarnings("rawtypes")
	@Bean
	ServletRegistrationBean h2servletRegistration(){
		@SuppressWarnings("unchecked")
		ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
	}

}
