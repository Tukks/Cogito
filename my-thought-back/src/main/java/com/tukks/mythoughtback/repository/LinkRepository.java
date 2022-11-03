package com.tukks.mythoughtback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.entity.LinkEntity;

@Service
public interface LinkRepository extends CrudRepository<LinkEntity, Long> {

	@Query("select p from LinkEntity p")
	List<LinkEntity> getAll();

}
