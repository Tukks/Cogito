package com.tukks.mythoughtback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tukks.mythoughtback.entity.ThoughtEntity;

public interface ThoughRepository extends JpaRepository<ThoughtEntity, Long> {

	List<ThoughtEntity> findByTitle(String title);

	@Query("select p from ThoughtEntity p")
	List<ThoughtEntity> getAll();

}
