package com.sergio.apianimals.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sergio.apianimals.model.entity.Animal;

public interface IAnimalDao extends JpaRepository<Animal, Long>{
	Optional<Animal> findById(Long id);
	Page<Animal> findAll(Pageable pageable);
	@Query("SELECT a FROM Animal a WHERE a.familyGroup.id = ?1")
	List<Animal> findAllByFamilyGroup(Long id);
}
