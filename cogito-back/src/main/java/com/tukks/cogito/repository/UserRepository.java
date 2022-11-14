package com.tukks.cogito.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tukks.cogito.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);
}
