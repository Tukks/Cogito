package com.tukks.cogito.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.tag.Tag;

@Service
public interface TagRepository extends CrudRepository<Tag, Long> {

	Tag getTagByTag(String tag);

}
