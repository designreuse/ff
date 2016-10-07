package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "company")
@NoArgsConstructor @Getter @Setter @ToString
public class Company extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Nationalized
	@Column(name = "code", nullable = true, length = 64)
	private String code;

	@OneToOne
	@JoinColumn(name = "\"user\"")
	private User user;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true)
	@OrderBy("id")
	private Set<CompanyItem> items;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "company_investment",
			joinColumns = { @JoinColumn(name = "company_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "investment_id", referencedColumnName = "id") },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"company_id", "investment_id"}) })
	private Set<Investment> investments;

}
