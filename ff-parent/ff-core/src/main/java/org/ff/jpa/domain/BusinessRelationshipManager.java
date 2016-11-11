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
@Table(name = "business_relationship_manager")
@NoArgsConstructor @Getter @Setter @ToString
public class BusinessRelationshipManager extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "first_name", nullable = true, length = 128)
	private String firstName;

	@Nationalized
	@Column(name = "last_name", nullable = true, length = 128)
	private String lastName;

	@Nationalized
	@Column(name = "phone", nullable = true, length = 128)
	private String phone;

	@Nationalized
	@Column(name = "mobile", nullable = true, length = 128)
	private String mobile;

	@Nationalized
	@Column(name = "email", nullable = true, length = 255)
	private String email;

}
