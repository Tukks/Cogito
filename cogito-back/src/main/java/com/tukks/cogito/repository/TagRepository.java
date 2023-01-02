package com.tukks.cogito.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.tag.Tag;

@Service
public interface TagRepository extends CrudRepository<Tag, Long> {

//	@Query("select p from Tag p where p.oidcSub = :sub and p.tag like ':tag' ")
//	List<Tag> getAll(String sub, String tag);

	List<Tag> getTagsByOidcSubAndHiddenAndTagStartsWithIgnoreCase(String sub,  Boolean hidden,  String tag);
}
