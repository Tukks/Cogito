package com.tukks.cogito.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.image.ImageEntity;

@Service
public interface ImageRepository extends CrudRepository<ImageEntity, Long> {

	ImageEntity getByIdAndOidcSub(Long id, String sub);

}
