package com.sergio.apianimals.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sergio.apianimals.model.entity.FamilyGroup;

public interface IFamilyGroupDao extends JpaRepository<FamilyGroup, Long>{
	Optional<FamilyGroup> findById(Long id);
	Optional<FamilyGroup> findByToken(String token);
}
