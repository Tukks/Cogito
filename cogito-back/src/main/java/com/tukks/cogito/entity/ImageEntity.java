package com.tukks.cogito.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

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
	@GeneratedValue
	@Column(columnDefinition = "uuid")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID id;

	private String oidcSub;

	private String imagePath;
}
