package com.tukks.cogito.repository;

import org.springframework.data.repository.CrudRepository;

import com.tukks.cogito.entity.TweetEntity;

public interface TweetRepository extends CrudRepository<TweetEntity, Long> {
}
