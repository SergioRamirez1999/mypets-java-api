package com.sergio.apianimals.controller;

import java.util.HashMap;
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
import com.sergio.apianimals.model.exceptions.FamilyBadFormatException;
import com.sergio.apianimals.model.exceptions.FamilyNotFoundException;
import com.sergio.apianimals.model.exceptions.FamilyNotSavedException;
import com.sergio.apianimals.model.exceptions.FamilyNotUpdatedException;
import com.sergio.apianimals.model.services.IFamilyGroupService;

@RestController
@RequestMapping("/apianimals/family-management/managed-families")
public class FamilyGroupController {
	
	@Autowired
	private IFamilyGroupService familyGroupService;
	
	private Logger logger = LoggerFactory.getLogger(FamilyGroupController.class);
	
	@GetMapping("/{id}")
	public ResponseEntity<Object>getFamily(@PathVariable Long id ) {
		try {
			Optional<FamilyGroup> familyGroup = familyGroupService.findOne(id);
			logger.info("OBTENCION DE FAMILIA: " + familyGroup.get());
			return ResponseEntity.ok(familyGroup.get());
		} catch (FamilyNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN OBTENCION DE FAMILIA");
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("")
	public ResponseEntity<Object> getFamiliesPagination(@RequestParam(name="page", defaultValue="0") int page){
		Pageable pageable = PageRequest.of(page, 10);
		Page<FamilyGroup> families = familyGroupService.findAll(pageable);
		if(families.isEmpty()) {
			logger.error("OBTENCION DE FAMILIAS PAGINADAS: SIN CONTENIDO");
			return ResponseEntity.noContent().build();
		}
		logger.error("OBTENCION DE FAMILIAS PAGINADAS: " + families.getContent());
		return ResponseEntity.ok(families.get());
	}
	
	@PostMapping("")
	public ResponseEntity<Object> saveFamily(@RequestBody FamilyGroup familyGroup){
		try {
			Optional<FamilyGroup> familySaved = familyGroupService.save(familyGroup);
			logger.info("SALVADO DE FAMILIA: " + familySaved.get());
			return ResponseEntity.ok(familySaved.get());
		} catch (FamilyBadFormatException | FamilyNotSavedException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN EL SALVADO DE FAMILIA: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateFamily(@RequestBody FamilyGroup familyGroup, @PathVariable Long id){
		try {
			Optional<FamilyGroup> familyUpdated = familyGroupService.update(familyGroup, id);
			logger.info("ACTUALIZACION DE FAMILIA: " + familyUpdated.get());
			return ResponseEntity.ok(familyUpdated.get());
		} catch (FamilyBadFormatException | FamilyNotFoundException | FamilyNotUpdatedException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.error("ERROR EN LA ACTUALIZACION DE FAMILIA: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removeFamily(@PathVariable Long id){
		try {
			Optional<FamilyGroup> familyRemoved = familyGroupService.remove(id);
			logger.info("ELIMINADO DE FAMILIA: " + familyRemoved.get());
			return ResponseEntity.ok(familyRemoved.get());
		} catch (FamilyNotFoundException e) {
			Map<String, String> message = new HashMap<>();
			message.put("message", e.getMessage());
			logger.info("ERROR EN EL ELIMINADO DE FAMILIA: " + e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}
}
