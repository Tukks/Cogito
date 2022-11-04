package com.tukks.mythoughtback.entity.tag;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Tag {

	@Id
	private int id;

	private String tag;
}
