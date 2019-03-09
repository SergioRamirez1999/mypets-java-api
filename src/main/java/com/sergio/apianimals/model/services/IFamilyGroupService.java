package com.sergio.apianimals.model.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sergio.apianimals.model.entity.FamilyGroup;
import com.sergio.apianimals.model.exceptions.FamilyBadFormatException;
import com.sergio.apianimals.model.exceptions.FamilyNotFoundException;
import com.sergio.apianimals.model.exceptions.FamilyNotSavedException;
import com.sergio.apianimals.model.exceptions.FamilyNotUpdatedException;

public interface IFamilyGroupService  {
	Optional<FamilyGroup> findOne(Long id) throws FamilyNotFoundException;
	Page<FamilyGroup> findAll(Pageable pageable);
	Optional<FamilyGroup> findByToken(String token) throws FamilyNotFoundException;
	Optional<FamilyGroup> save(FamilyGroup familyGroup) throws FamilyBadFormatException, FamilyNotSavedException;
	Optional<FamilyGroup> update(FamilyGroup familyGroup, Long id) throws FamilyBadFormatException, FamilyNotFoundException, FamilyNotUpdatedException; 
	Optional<FamilyGroup> remove(Long id) throws FamilyNotFoundException;

}
