package com.sergio.apianimals.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sergio.apianimals.model.dao.IAnimalDao;
import com.sergio.apianimals.model.entity.Animal;
import com.sergio.apianimals.model.exceptions.AnimalBadFormatException;
import com.sergio.apianimals.model.exceptions.AnimalNotFoundException;
import com.sergio.apianimals.model.exceptions.AnimalNotSavedException;
import com.sergio.apianimals.model.exceptions.AnimalNotUpdatedException;

@Service
public class AnimalServiceImpl implements IAnimalService {
	
	@Autowired
	private IAnimalDao animalDao;

	@Override
	public Optional<Animal> findOne(Long id) throws AnimalNotFoundException {
		Optional<Animal> animal = animalDao.findById(id);
		if(!animal.isPresent()) {
			throw new AnimalNotFoundException("EL ANIMAL NO SE ENCUENTRA EN LA BBDD");
		}
		return animal;
	}

	@Override
	public Page<Animal> findAll(Pageable pageable) {
		return animalDao.findAll(pageable);
	}
	
	@Override
	public List<Animal> findByFamilyGroup(Long id) {
		List<Animal> lstAnimals = animalDao.findAllByFamilyGroup(id);
		return lstAnimals;
	}
	
	@Override
	public Optional<Animal> save(Animal animal) throws AnimalBadFormatException, AnimalNotSavedException {
		if(!isAnimalValid(animal)) {
			throw new AnimalBadFormatException("EL ANIMAL NO CUMPLE CON EL FORMATO");
		}
		animal.setFamilyGroup(null);
		Optional<Animal> animalSaved = Optional.of(animalDao.save(animal));
		if(!animalSaved.isPresent()) {
			throw new AnimalNotSavedException("EL ANIMAL NO SE HA SALVADO");
		}
		return animalSaved;
	}
	@Override
	public Optional<Animal> update(Animal animal, Long id) throws AnimalBadFormatException, AnimalNotUpdatedException, AnimalNotFoundException {
		if(!isAnimalValid(animal)) {
			throw new AnimalBadFormatException("EL ANIMAL NO CUMPLE CON EL FORMATO");
		}
		Optional<Animal> animalExists = this.findOne(id);
		if(!animalExists.isPresent()) {
			throw new AnimalNotFoundException("EL ANIMAL NO SE ENCUENTRA EN LA BBDD");
		}
		animal.setId(id);
		Optional<Animal> animalUpdated = Optional.of(animalDao.save(animal));
		if(!animalUpdated.isPresent()) {
			throw new AnimalNotUpdatedException("EL ANIMAL NO SE HA ACTUALIZADO");
		}
		return  animalUpdated;
	}

	@Override
	public Optional<Animal> remove(Long id) throws AnimalNotFoundException {
		Optional<Animal> animalToRemove = this.findOne(id);
		if(!animalToRemove.isPresent()) {
			throw new AnimalNotFoundException("EL ANIMAL NO SE ENCUENTRA EN LA BBDD");
		}
		animalDao.deleteById(id);
		return animalToRemove;
	}
	
	private boolean isAnimalValid(Animal animal) {
		if((animal.getName() != null && animal.getName() != "")
				&& (animal.getBirthdate() != null)
				&& (animal.getWeight() != null)
				&& (animal.getAnimalType() != null)
				) {
			return true;
		}
		return false;
	}

}
