package com.tukks.mythoughtback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.entity.NoteEntity;

@Service
public interface NoteRepository extends CrudRepository<NoteEntity, Long> {

	NoteEntity getById(Long id);
}
