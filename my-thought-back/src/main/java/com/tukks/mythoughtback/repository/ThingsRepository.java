package com.tukks.mythoughtback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.entity.ThingsEntity;

@Service
public interface ThingsRepository extends JpaRepository<ThingsEntity, Long> {

	@Query("select p from ThingsEntity p")
	List<Object> getAll();

}
