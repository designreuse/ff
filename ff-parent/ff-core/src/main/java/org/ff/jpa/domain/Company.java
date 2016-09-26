package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "company")
@NoArgsConstructor @Getter @Setter @ToString
@Audited
@JsonInclude(Include.NON_NULL)
public class Company extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "\"user\"")
	@AuditOverride(name = "\"user\"")
	private User user;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Nationalized
	@Column(name = "code", nullable = false, length = 64)
	private String code;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "company_investment",
			joinColumns = { @JoinColumn(name = "company_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "investment_id", referencedColumnName = "id") },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"company_id", "investment_id"}) })
	@JsonIgnore
	private Set<Investment> investments;

}
