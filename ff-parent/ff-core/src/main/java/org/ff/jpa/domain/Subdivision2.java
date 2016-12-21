package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "subdivision2")
@NoArgsConstructor @Getter @Setter @ToString
public class Subdivision2 extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@ManyToOne
	@JoinColumn(name = "subdivision1", nullable = true)
	private Subdivision1 subdivision1;

	@Column(name = "development_index", nullable = true, length = 32)
	private String developmentIndex;

}
