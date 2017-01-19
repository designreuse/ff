package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "impression")
@NoArgsConstructor @Getter @Setter @ToString
public class Impression extends AbstractEntity {

	public enum EntityType { ARTICLE, TENDER, USER };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type", nullable = false, length = 16)
	private EntityType entityType;

	@Column(name = "entity_id", nullable = false)
	private Integer entityId;

}
