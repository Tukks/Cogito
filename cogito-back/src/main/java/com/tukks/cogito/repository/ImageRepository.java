
package com.tukks.cogito.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.ImageEntity;

@Service
public interface ImageRepository extends CrudRepository<ImageEntity, Long> {

	ImageEntity getByIdAndOidcSub(UUID id, String sub);

}
