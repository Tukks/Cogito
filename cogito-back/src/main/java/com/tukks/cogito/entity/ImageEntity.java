package com.tukks.cogito.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class ImageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String oidcSub;

	@Lob
	@Column(name = "imagePath")
	private String imagePath;

}
