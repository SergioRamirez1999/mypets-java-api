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

import com.sergio.apianimals.model.entity.Animal;
import com.sergio.apianimals.model.entity.FamilyGroup;
import com.sergio.apianimals.model.exceptions.AnimalBadFormatException;
import com.sergio.apianimals.model.exceptions.AnimalNotFoundException;
import com.sergio.apianimals.model.exceptions.AnimalNotSavedException;
import com.sergio.apianimals.model.exceptions.AnimalNotUpdatedException;
import com.sergio.apianimals.model.exceptions.FamilyNotFoundException;
import com.sergio.apianimals.model.services.IAnimalService;
import com.sergio.apianimals.model.services.IFamilyGroupService;

@RestController
@RequestMapping("/apianimals/animal-management/managed-animals")
public class AnimalController {
	
	@Autowired
	private IAnimalService animalService;
	
	@Autowired
	private IFamilyGroupService familyService;
	
	private Logger logger = LoggerFactory.getLogger(AnimalController.class);
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getAnimal(@PathVariable Long id){
		try {
			Optional<Animal> animal = animalService.findOne(id);
			logger.info("OBTENCION DE ANIMAL: " + animal.get());
			return ResponseEntity.ok(animal.get());
		} catch (AnimalNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN OBTENCION DE ANIMAL");
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("")
	public ResponseEntity<Object> getAnimalsPagination(@RequestParam(name="page", defaultValue="0") int page){
		Pageable pageable = PageRequest.of(page, 10);
		Page<Animal> animals = animalService.findAll(pageable);
		if(animals.isEmpty()) {
			logger.error("OBTENCION DE ANIMALES PAGINADOS: SIN CONTENIDO");
			return ResponseEntity.noContent().build();
		}
		logger.info("OBTENCION DE ANIMALES PAGINADOS: " + animals.getContent());
		return ResponseEntity.ok(animals);
	}
	
	@GetMapping("/family")
	public ResponseEntity<Object> getAnimalsByFamily(@RequestParam(name="family_id")Long id){
		try {
			Optional<FamilyGroup> family = familyService.findOne(id);
			List<Animal> animals = animalService.findByFamilyGroup(family.get().getId());
			if(animals.isEmpty()) {
				logger.error("OBTENCION DE ANIMALES POR FAMILIA: SIN CONTENIDO");
				return ResponseEntity.noContent().build();
			}
			logger.info("OBTENCION DE ANIMALES POR FAMILIA: " + animals);
			return ResponseEntity.ok(animals);
		
		} catch (FamilyNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN OBTENCION DE ANIMAL POR FAMILIA: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("")
	public ResponseEntity<Object> saveAnimal(@RequestBody Animal animal){
		try {
			Optional<Animal> animalSaved = animalService.save(animal);
			logger.info("SALVADO DE USUARIO: " + animalSaved.get());
			return ResponseEntity.ok(animalSaved);
		} catch (AnimalBadFormatException | AnimalNotSavedException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL SALVADO DE ANIMAL: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateAnimal(@RequestBody Animal animal, @PathVariable Long id){
		try {
			Optional<Animal> animalUpdated = animalService.update(animal, id);
			logger.info("ACTUALIZACION DE ANIMAL: " + animalUpdated.get());
			return ResponseEntity.ok(animalUpdated);
		} catch (AnimalBadFormatException | AnimalNotUpdatedException | AnimalNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN LA ACTUALIZACION DE ANIMAL: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);		}

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removeAnimal(@PathVariable Long id){
		try {
			Optional<Animal> animalRemoved = animalService.remove(id);
			logger.info("ELIMINADO DE ANIMAL: " + animalRemoved.get());
			return ResponseEntity.ok(animalRemoved);
		} catch (Exception e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.info("ERROR EN EL ELIMINADO DE ANIMAL: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
}
