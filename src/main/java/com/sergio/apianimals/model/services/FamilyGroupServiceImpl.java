package com.sergio.apianimals.model.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sergio.apianimals.model.dao.IFamilyGroupDao;
import com.sergio.apianimals.model.entity.FamilyGroup;
import com.sergio.apianimals.model.exceptions.FamilyBadFormatException;
import com.sergio.apianimals.model.exceptions.FamilyNotFoundException;
import com.sergio.apianimals.model.exceptions.FamilyNotSavedException;
import com.sergio.apianimals.model.exceptions.FamilyNotUpdatedException;

@Service
public class FamilyGroupServiceImpl implements IFamilyGroupService {
	
	@Autowired
	private IFamilyGroupDao familyGroupDao;
	
	Logger logger = LoggerFactory.getLogger(FamilyGroupServiceImpl.class);

	@Override
	public Optional<FamilyGroup> findOne(Long id) throws FamilyNotFoundException {
		Optional<FamilyGroup> family = familyGroupDao.findById(id);
		if(!family.isPresent()) {
			throw new FamilyNotFoundException("EL USUARIO NO SE ENCUENTRA EN LA BBDD");
		}
		return family;
	}
	

	@Override
	public Page<FamilyGroup> findAll(Pageable pageable) {
		return familyGroupDao.findAll(pageable);
	}
	
	@Override
	public Optional<FamilyGroup> findByToken(String token) throws FamilyNotFoundException {
		Optional<FamilyGroup> family = familyGroupDao.findByToken(token);
		if(!family.isPresent()) {
			throw new FamilyNotFoundException("LA FAMILIA NO SE ENCUENTRA EN LA BBDD");
		}
		return family;
	}
	
	@Override
	public Optional<FamilyGroup> save(FamilyGroup familyGroup) throws FamilyBadFormatException, FamilyNotSavedException {
		if(!isFamilyValid(familyGroup)) {
			throw new FamilyBadFormatException("LA FAMILIA NO CUMPLE CON EL FORMATO");
		}
		Optional<FamilyGroup> familySaved = Optional.of(familyGroupDao.save(familyGroup));
		if(!familySaved.isPresent()) {
			throw new FamilyNotSavedException("LA FAMILIA NO SE HA SALVADO");
		}
		return familySaved;
	}

	@Override
	public Optional<FamilyGroup> update(FamilyGroup familyGroup, Long id) throws FamilyBadFormatException, FamilyNotFoundException, FamilyNotUpdatedException {
		Optional<FamilyGroup> familyExists = this.findOne(id);
		if(!isFamilyValid(familyGroup)) {
			throw new FamilyBadFormatException("LA FAMILIA NO CUMPLE CON EL FORMATO");
		}
		if(!familyExists.isPresent()) {
			throw new FamilyNotFoundException("LA FAMILIA NO SE ENCUENTRA EN LA BBDD");
		}
		familyGroup.setId(id);
		Optional<FamilyGroup> familyUpdated = Optional.of(familyGroupDao.save(familyGroup));
		if(!familyUpdated.isPresent()) {
			throw new FamilyNotUpdatedException("LA FAMILIA NO SE HA ACTUALIZADO");
		}
		return familyUpdated;
	}

	@Override
	public Optional<FamilyGroup> remove(Long id)  throws FamilyNotFoundException {
		Optional<FamilyGroup> familyExists = this.findOne(id);
		if(!familyExists.isPresent()) {
			throw new FamilyNotFoundException("LA FAMILIA NO SE ENCUENTRA EN LA BBDD");
		}
		familyGroupDao.deleteById(id);
		return familyExists;
	}
	
	public boolean isFamilyValid(FamilyGroup family) {
		if((family.getToken() != null && family.getToken() != "")) {
			return true;
		}
		return false;
	}
}
