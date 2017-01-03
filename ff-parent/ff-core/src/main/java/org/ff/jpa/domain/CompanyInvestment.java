package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "company_investment")
@NoArgsConstructor @Getter @Setter @ToString
public class CompanyInvestment extends AbstractEntity {

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

	@ManyToOne
	@JoinColumn(name = "investment", nullable = true)
	private Investment investment;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInvestment", orphanRemoval = true)
	@OrderBy("id")
	private Set<CompanyInvestmentItem> items;

}
