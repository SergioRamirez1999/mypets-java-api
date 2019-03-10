package com.sergio.apianimals.model.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sergio.apianimals.model.dao.IUserDao;
import com.sergio.apianimals.model.entity.Authority;
import com.sergio.apianimals.model.entity.RoleType;
import com.sergio.apianimals.model.entity.User;
import com.sergio.apianimals.model.exceptions.EmailDuplicatedException;
import com.sergio.apianimals.model.exceptions.UserBadFormatException;
import com.sergio.apianimals.model.exceptions.UserNotFoundException;
import com.sergio.apianimals.model.exceptions.UserNotSavedException;
import com.sergio.apianimals.model.exceptions.UserNotUpdatedException;

@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private IUserDao userDao;
	
	@Lazy
	@Autowired
	private BCryptPasswordEncoder psw;
	
	@Override
	public Optional<User> findOne(Long id) throws UserNotFoundException {
		Optional<User> user = userDao.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("EL USUARIO NO SE ENCUENTRA EN LA BBDD");
		}
		return user;
	}
	
	@Override
	public Optional<User> findByEmail(String email) throws UserNotFoundException {
		Optional<User> user = userDao.findByEmail(email);
		if(!user.isPresent()) {
			throw new UserNotFoundException("EL USUARIO NO SE ENCUENTRA EN LA BBDD");
		}
		return user;
	}
	@Override
	public Page<User> findAll(Pageable pageable) {
		return  userDao.findAll(pageable);
	}
	
	
	@Override
	public List<User> findAllByFamilyGroup(Long id) {
		List<User> users = userDao.findAllByFamilyGroup(id);
		return users;
	}
	
	@Override
	public Optional<User> save(User user) throws UserBadFormatException, EmailDuplicatedException, UserNotSavedException {
		if(!isUserValid(user)) {
			if(isDuplicatedEmail(user.getEmail())) {
				throw new EmailDuplicatedException("EL EMAIL YA SE ENCUENTRA REGISTRADO: " + user.getEmail());
			}
			throw new UserBadFormatException("EL USUARIO NO CUMPLE CON EL FORMATO");
		}
		user.setEmail(user.getEmail().toLowerCase());
		user.setAuthorities(Arrays.asList(new Authority(RoleType.USER)));
		user.setPassword(psw.encode(user.getPassword()));

		Optional<User> userSaved = Optional.of(userDao.save(user));
		if(!userSaved.isPresent()) {
			throw new UserNotSavedException("EL USUARIO NO SE HA SALVADO");
		}
		return userSaved;
	}

	@Override
	public Optional<User> update(User user, Long id) throws UserBadFormatException, UserNotFoundException, UserNotUpdatedException {
		if(!isUserValid(user) && user.getEnabled() != null && user.getAuthorities() != null) {
			throw new UserBadFormatException("EL USUARIO NO CUMPLE CON EL FORMATO");
		}
		Optional<User> userExist = this.findOne(id);
		if(!userExist.isPresent()) {
			throw new UserNotFoundException("EL USUARIO NO SE ENCUENTRA EN LA BBDD");
		}
		user.setId(id);
		Optional<User> userUpdated = Optional.of(userDao.save(user));
		if(!userUpdated.isPresent()) {
			throw new UserNotUpdatedException("EL USUARIO NO SE HA ACTUALIZADO");
		}
		return userUpdated;
	}

	@Override
	public Optional<User> remove(Long id) throws UserNotFoundException {
		Optional<User> userRemoved = this.findOne(id);
		if(!userRemoved.isPresent()) {
			throw new UserNotFoundException("EL USUARIO NO SE ENCUENTRA EN LA BBDD");
		}
		userDao.deleteById(id);
		return userRemoved;
	}
	
	private boolean isUserValid(User user) {
		if((user.getName() != null && user.getName() != "") 
				&& (user.getLastName() != null && user.getLastName() != "")
				&& (user.getEmail() != null && user.getEmail() != "")
				) {
			
			return true;
		}
		return false;
	}
	
	private boolean isDuplicatedEmail(String email) {
		Optional<User> user = userDao.findByEmail(email);
		if(user.isPresent()) {
			return true;
		}
		return false;
	}
	
}
