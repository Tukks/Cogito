package com.tukks.mythoughtback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.entity.LinkEntity;
import com.tukks.mythoughtback.entity.NoteEntity;

@Service
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

	@Query("select p from NoteEntity p")
	List<LinkEntity> getAll();

}
