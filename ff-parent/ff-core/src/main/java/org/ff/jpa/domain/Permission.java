package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "permission")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Permission {

	@Id
	@Column(name = "id")
	private Integer id;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

}
