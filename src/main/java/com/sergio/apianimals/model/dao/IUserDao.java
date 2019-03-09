package com.sergio.apianimals.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sergio.apianimals.model.entity.User;

public interface IUserDao extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
	Optional<User> findByEmail(String email);
	@Query("SELECT u FROM User u WHERE u.familyGroup.id = ?1")
	List<User> findAllByFamilyGroup(Long id);
}
