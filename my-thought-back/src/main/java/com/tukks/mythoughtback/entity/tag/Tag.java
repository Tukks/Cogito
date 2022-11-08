package com.tukks.mythoughtback.entity.tag;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Jacksonized
@Builder
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String tag;

}
