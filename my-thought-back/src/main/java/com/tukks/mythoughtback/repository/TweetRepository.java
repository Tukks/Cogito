package com.tukks.mythoughtback.repository;

import com.tukks.mythoughtback.entity.TweetEntity;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<TweetEntity, Long> {
}
