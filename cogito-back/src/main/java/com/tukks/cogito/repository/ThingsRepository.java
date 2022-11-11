package com.tukks.cogito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.ThingsEntity;

@Service
public interface ThingsRepository extends CrudRepository<ThingsEntity, Long> {

	@Query("select p from ThingsEntity p where p.oidcSub = :sub order by p.modified desc")
	List<Object> getAll(String sub);

	ThingsEntity getByIdAndOidcSub(Long id, String sub);

	ThingsEntity deleteByIdAndOidcSub(Long id, String sub);

}
