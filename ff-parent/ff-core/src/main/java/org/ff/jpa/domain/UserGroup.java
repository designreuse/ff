package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "user_group")
@NoArgsConstructor @Getter @Setter @ToString
public class UserGroup extends AbstractEntity {

	public enum UserGroupStatus { ACTIVE, INACTIVE };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private UserGroupStatus status;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@ManyToMany
	@JoinTable(
			name = "user_group_user",
			joinColumns = { @JoinColumn(name = "user_group_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"user_group_id", "user_id"}) })
	private Set<User> users;

}
