package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contact")
@NoArgsConstructor @Getter @Setter @ToString
public class Contact extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "company_name", nullable = true, length = 255)
	private String companyName;

	@Nationalized
	@Column(name = "company_code", nullable = true, length = 255)
	private String companyCode;

	@Nationalized
	@Column(name = "name", nullable = true, length = 255)
	private String name;

	@Nationalized
	@Column(name = "email", nullable = true, length = 255)
	private String email;

	@Nationalized
	@Column(name = "phone", nullable = true, length = 255)
	private String phone;

	@Lob
	@Nationalized
	@Column(name = "location", nullable = true)
	private String location;

	@Lob
	@Nationalized
	@Column(name = "text", nullable = true)
	private String text;

}
