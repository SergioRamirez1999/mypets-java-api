package com.sergio.apianimals.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sergio.apianimals.model.entity.User;
import com.sergio.apianimals.model.exceptions.EmailDuplicatedException;
import com.sergio.apianimals.model.exceptions.UserBadFormatException;
import com.sergio.apianimals.model.exceptions.UserNotFoundException;
import com.sergio.apianimals.model.exceptions.UserNotSavedException;
import com.sergio.apianimals.model.exceptions.UserNotUpdatedException;


public interface IUserService {
	Optional<User> findOne(Long id) throws UserNotFoundException;
	Optional<User> findByEmail(String email) throws UserNotFoundException;
	List<User> findAllByFamilyGroup(Long id);
	Page<User> findAll(Pageable pageable);
	Optional<User> save(User user) throws UserBadFormatException, EmailDuplicatedException, UserNotSavedException;
	Optional<User> update(User user, Long id) throws UserBadFormatException, UserNotFoundException, UserNotUpdatedException;
	Optional<User> remove(Long id) throws UserNotFoundException;
}
