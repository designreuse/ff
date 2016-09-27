package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "nkd")
@NoArgsConstructor @Getter @Setter @ToString
public class Nkd extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "sector", nullable = false, length = 8)
	private String sector;

	@Nationalized
	@Column(name = "sector_name", nullable = false, length = 512)
	private String sectorName;

	@Nationalized
	@Column(name = "area", nullable = false, length = 8)
	private String area;

	@Nationalized
	@Column(name = "activity", nullable = false, length = 8)
	private String activity;

	@Nationalized
	@Column(name = "activity_name", nullable = false, length = 512)
	private String activityName;

}
