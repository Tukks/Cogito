package com.tukks.cogito.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("NOTE")
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public class NoteEntity extends ThingsEntity {

	@Lob
	private String markdown;
}
