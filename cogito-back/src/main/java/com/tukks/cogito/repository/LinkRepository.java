package com.tukks.cogito.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.LinkEntity;

@Service
public interface LinkRepository extends CrudRepository<LinkEntity, Long> {

	@Query("select p from LinkEntity p")
	List<LinkEntity> getAll();

	@Query("select e from LinkEntity e left join fetch e.tags b where e.id = ?1")
	LinkEntity findById(UUID id);

}
