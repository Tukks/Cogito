package com.tukks.mythoughtback.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NOTE")
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("NOTE")
public class NoteEntity extends ThingsEntity {

	@Lob
	private String markdown;
}
