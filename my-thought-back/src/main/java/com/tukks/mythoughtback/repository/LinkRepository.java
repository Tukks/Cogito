package com.tukks.mythoughtback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.entity.LinkEntity;

@Service
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {

	@Query("select p from LinkEntity p")
	List<LinkEntity> getAll();

}
