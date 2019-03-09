package com.sergio.apianimals.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sergio.apianimals.model.entity.Animal;
import com.sergio.apianimals.model.entity.FamilyGroup;
import com.sergio.apianimals.model.entity.User;
import com.sergio.apianimals.model.exceptions.AnimalBadFormatException;
import com.sergio.apianimals.model.exceptions.AnimalNotFoundException;
import com.sergio.apianimals.model.exceptions.AnimalNotUpdatedException;
import com.sergio.apianimals.model.exceptions.FamilyNotFoundException;
import com.sergio.apianimals.model.exceptions.UserBadFormatException;
import com.sergio.apianimals.model.exceptions.UserNotFoundException;
import com.sergio.apianimals.model.exceptions.UserNotUpdatedException;
import com.sergio.apianimals.model.services.IAnimalService;
import com.sergio.apianimals.model.services.IFamilyGroupService;
import com.sergio.apianimals.model.services.IUserService;
import com.sergio.apianimals.smtp.entity.EmailReceiver;
import com.sergio.apianimals.smtp.services.SendEmailService;

@RestController
@RequestMapping("/apianimals/services")
public class ServicesController {
	
	private static final String PATH_UPLOAD_IMAGE = "/home/lechos00/Documents/programming/workspace-spring-tool-suite-4-4.0.0.RELEASE/apianimals2/uploads";
	
	@Autowired
	private IAnimalService animalService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IFamilyGroupService familyGroupService;
	
	@Autowired
	private SendEmailService sendEmailService;
	
	private Logger logger = LoggerFactory.getLogger(ServicesController.class);

	@PutMapping("/users/activate/{id}")
	public ResponseEntity<Object> activateUser(@PathVariable Long id) {
		try {
			Optional<User> userToActivate = userService.findOne(id);
			userToActivate.get().setEnabled(true);
			Optional<User> userActivated = userService.update(userToActivate.get(), id);
			logger.info("ACTIVACION DE USUARIO: " + userActivated.get());
			return ResponseEntity.ok(userActivated.get());
		} catch (UserBadFormatException | UserNotFoundException | UserNotUpdatedException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN LA ACTIVACION DE USUARIO: " + e.getMessage() );
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}

	@PutMapping("/users/joinfamily")
	public ResponseEntity<Object> joinUser(@RequestParam(name="user_id") Long user_id,
															@RequestParam(name="family_token") String token) {
		
		try {
			Optional<User> userToJoin = userService.findOne(user_id);
			Optional<FamilyGroup> familyHost  = familyGroupService.findByToken(token); 
			userToJoin.get().setFamilyGroup(familyHost.get());
			Optional<User> userJoined = userService.update(userToJoin.get(), user_id);
			logger.info("JOIN DE USUARIO A FAMILIA: " + userJoined.get());
			return ResponseEntity.ok(userJoined);
		} catch (UserBadFormatException | UserNotFoundException | UserNotUpdatedException | FamilyNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL  JOIN DEL USUARIO A  FAMILIA: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
	}

	@PutMapping("/animals/joinfamily")
	public ResponseEntity<Object> joinAnimal(@RequestParam(name="family_id") Long family_id, 
																@RequestParam(name="animal_id") Long animal_id) {
		
		try {
			Optional<Animal> animalToJoin = animalService.findOne(animal_id);
			Optional<FamilyGroup> familyHost = familyGroupService.findOne(family_id);
			animalToJoin.get().setFamilyGroup(familyHost.get());
			Optional<Animal> animalJoined = animalService.update(animalToJoin.get(), animal_id);
			logger.info("JOIN DE ANIMAL A FAMILIA: " + animalJoined.get());
			return ResponseEntity.ok(animalJoined);
		} catch (FamilyNotFoundException| AnimalBadFormatException | AnimalNotUpdatedException | AnimalNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL  JOIN DEL ANIMAL  A  FAMILIA: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/email/verification")
	public ResponseEntity<Object> sendVerificationEmail(@RequestBody EmailReceiver toReceiver){
		String code = RandomStringUtils.random(5, true, true);
		sendEmailService.sendVerificationEmail(toReceiver, code);
		Map<String, String> message = new HashMap<>();
		message.put("code", code);
		logger.info("ENVIO EMAIL DE VERIFIACION: email=" + toReceiver.getEmail() + ", codigo=" + code);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@PostMapping("/upload/image")
	public ResponseEntity<Object > uploadImage(@RequestParam(name="image") MultipartFile image){
		Path fileNameAndPath = Paths.get(PATH_UPLOAD_IMAGE, image.getOriginalFilename());
		try {
			Files.write(fileNameAndPath, image.getBytes());
			Map<String, String> message = new HashMap<>();
			message.put("image", image.getOriginalFilename());
			logger.info("UPLOAD DE IMAGEN: " + image.getOriginalFilename());
			return ResponseEntity.ok(message);
		} catch (IOException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL UPLOAD DE IMAGEN: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/download/image")
	public ResponseEntity<Object> getImage(@RequestParam(name="filename") String filename){
		Path fileNameAndPath = Paths.get(PATH_UPLOAD_IMAGE, filename);
		File file = fileNameAndPath.toFile();
		try {
			Resource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			logger.info("OBTENCION DE IMAGEN: " + filename);
			return new ResponseEntity<>(resource, headers, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN LA OBTENCION DE IMAGEN: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);		
		}
	}
	
}
