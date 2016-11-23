package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "role")
@NoArgsConstructor @Getter @Setter @ToString
public class Role extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@ManyToMany
	@JoinTable(
			name = "role_permission",
			joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName = "id") },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"role_id", "permission_id"}) })
	private Set<Permission> permissions;

}
