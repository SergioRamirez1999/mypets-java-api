package com.sergio.apianimals.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sergio.apianimals.model.entity.FamilyGroup;
import com.sergio.apianimals.model.entity.User;
import com.sergio.apianimals.model.exceptions.EmailDuplicatedException;
import com.sergio.apianimals.model.exceptions.FamilyNotFoundException;
import com.sergio.apianimals.model.exceptions.UserBadFormatException;
import com.sergio.apianimals.model.exceptions.UserNotFoundException;
import com.sergio.apianimals.model.exceptions.UserNotSavedException;
import com.sergio.apianimals.model.exceptions.UserNotUpdatedException;
import com.sergio.apianimals.model.services.IFamilyGroupService;
import com.sergio.apianimals.model.services.IUserService;


@RestController
@RequestMapping("/apianimals/user-management/managed-users")
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IFamilyGroupService familyService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id){
		try {
			Optional<User> user = userService.findOne(id);
			logger.info("OBTENCION DE USUARIO: " + user.get());
			return ResponseEntity.ok(user.get());
		} catch (UserNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN OBTENCION DE USUARIO");
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getbyemail/{email}")
	public ResponseEntity<Object> getUserByEmail(@PathVariable String email){
		try {
			Optional<User> user = userService.findByEmail(email);
			logger.info("OBTENCION DE USUARIO: " + user.get());
			return ResponseEntity.ok(user.get());
		} catch (UserNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN OBTENCION DE USUARIO");
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("")
	public ResponseEntity<Object> getUsersPagination(@RequestParam(name = "page", defaultValue = "0") int page){
		Pageable pageable = PageRequest.of(page, 10);
		Page<User> users = userService.findAll(pageable);
		if(users.isEmpty()) {
			logger.error("OBTENCION DE USUARIOS PAGINADOS: SIN CONTENIDO");
			return ResponseEntity.noContent().build();
		}
		logger.info("OBTENCION DE USUARIOS PAGINADOS: " + users.getContent());
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/family")
	public ResponseEntity<Object> getUsersByFamily(@RequestParam(name="family_id") Long id){
		try {
			Optional<FamilyGroup> family = familyService.findOne(id);
			List<User> users = userService.findAllByFamilyGroup(family.get().getId());
			if(users.isEmpty()) {
				logger.error("OBTENCION DE USUARIOS POR FAMILIA: SIN CONTENIDO");
				return ResponseEntity.noContent().build();
			}
			logger.info("OBTENCION DE USUARIOS POR FAMILIA: " + users);
			return ResponseEntity.ok(users);
		} catch (FamilyNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN OBTENCION DE USUARIOS POR FAMILIA");
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("")
	public ResponseEntity<Object> saveUser(@RequestBody User user){
		try {
			Optional<User> userSaved = userService.save(user);
			logger.info("SALVADO DE USUARIO: " + userSaved.get());
			return ResponseEntity.ok(userSaved.get());
		} catch (UserBadFormatException | EmailDuplicatedException | UserNotSavedException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL SALVADO DE USUARIO: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable Long id){
		try {
			Optional<User> userUpdated = userService.update(user, id);
			logger.info("ACTUALIZACION DE USUARIO: " + userUpdated.get());
			return ResponseEntity.ok(userUpdated.get());
		} catch (UserBadFormatException | UserNotFoundException | UserNotUpdatedException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN LA ACTUALIZACION DE USUARIO: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removeUser(@PathVariable Long id){
		try {
			Optional<User> userRemoved = userService.remove(id);
			logger.info("ELIMINADO DE USUARIO: " + userRemoved.get());
			return ResponseEntity.ok(userRemoved.get());
		} catch (UserNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL ELIMINADO DE USUARIO: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
}
