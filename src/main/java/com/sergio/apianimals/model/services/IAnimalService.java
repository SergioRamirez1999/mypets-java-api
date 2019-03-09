package com.sergio.apianimals.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sergio.apianimals.model.entity.Animal;
import com.sergio.apianimals.model.exceptions.AnimalBadFormatException;
import com.sergio.apianimals.model.exceptions.AnimalNotFoundException;
import com.sergio.apianimals.model.exceptions.AnimalNotSavedException;
import com.sergio.apianimals.model.exceptions.AnimalNotUpdatedException;

public interface IAnimalService {
	Optional<Animal> findOne(Long id) throws AnimalNotFoundException;
	Page<Animal> findAll(Pageable pageable);
	List<Animal> findByFamilyGroup(Long id);
	Optional<Animal> save(Animal animal) throws AnimalBadFormatException, AnimalNotSavedException;
	Optional<Animal> update(Animal animal, Long id) throws AnimalBadFormatException, AnimalNotUpdatedException, AnimalNotFoundException;
	Optional<Animal> remove(Long ig) throws AnimalNotFoundException;
}
