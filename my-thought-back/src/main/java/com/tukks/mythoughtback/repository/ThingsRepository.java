package com.tukks.mythoughtback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.entity.ThingsEntity;

@Service
public interface ThingsRepository extends CrudRepository<ThingsEntity, Long> {

	@Query("select p from ThingsEntity p")
	List<Object> getAll();

}
