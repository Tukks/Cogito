package com.tukks.cogito.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.NoteEntity;

@Service
public interface NoteRepository extends CrudRepository<NoteEntity, Long> {

	NoteEntity getByIdAndOidcSub(UUID id, String sub);

}
