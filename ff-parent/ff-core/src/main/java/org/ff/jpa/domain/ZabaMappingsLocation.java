package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "zaba_mappings_location")
@NoArgsConstructor @Getter @Setter @ToString
public class ZabaMappingsLocation {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "zip_code", nullable = true, length = 16)
	private String zipCode;

	@Nationalized
	@Column(name = "subdivision2", nullable = true, length = 255)
	private String subdivision2;

}
