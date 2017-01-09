package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "project")
@NoArgsConstructor @Getter @Setter @ToString
public class Project extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Lob
	@Nationalized
	@Column(name = "description", nullable = true)
	private String description;

	@ManyToOne
	@JoinColumn(name = "company", nullable = true)
	private Company company;

	@ManyToMany
	@JoinTable(
			name = "project_investment",
			joinColumns = { @JoinColumn(name = "project_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "investment_id", referencedColumnName = "id") },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"project_id", "investment_id"}) })
	private Set<Investment> investments;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
	@OrderBy("id")
	private Set<ProjectItem> items;

}
