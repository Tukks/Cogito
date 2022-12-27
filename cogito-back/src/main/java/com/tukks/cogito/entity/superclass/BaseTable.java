package com.tukks.cogito.entity.superclass;

import java.time.Instant;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
public class BaseTable {

	@CreationTimestamp
	private Instant created;

	@UpdateTimestamp
	private Instant modified;

}
