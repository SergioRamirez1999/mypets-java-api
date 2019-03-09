package com.sergio.apianimals.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sergio.apianimals.model.dao.IUserDao;
import com.sergio.apianimals.model.entity.Authority;
import com.sergio.apianimals.model.entity.User;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private IUserDao userDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userDao.findByEmail(email.toLowerCase());
		if(!user.isPresent()) {
			logger.error("ERROR LOGIN: USER " + email + "NO ENCONTRADO");
			throw new UsernameNotFoundException("ERROR LOGIN: USER " + email + " NO ENCONTRADO");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for(Authority tmpAuthority: user.get().getAuthorities()) {
	        	logger.info("Role: ".concat(tmpAuthority.getRole().toString()));
			authorities.add(new SimpleGrantedAuthority(tmpAuthority.getRole().toString()));
		}
		
		if(authorities.isEmpty()) {
			logger.error("ERROR LOGIN: USER " + email + "SIN ROLES ASIGNADOS");
			throw new UsernameNotFoundException("ERROR LOGIN: USER " + email + " SIN ROLES ASIGNADOS");
		}
		
		return new org.springframework.security.core.userdetails.User(user.get().getEmail(), 
				user.get().getPassword(), 
				user.get().getEnabled(), 
				true, 
				true, 
				true, 
				authorities );
		
		
	}

}
