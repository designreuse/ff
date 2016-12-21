package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "company_item")
@NoArgsConstructor @Getter @Setter @ToString
public class CompanyItem extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "item", nullable = true)
	private Item item;

	@Lob
	@Nationalized
	@Column(name = "value", nullable = true)
	private String value;

	@Nationalized
	@Column(name = "currency", nullable = true, length = 8)
	private String currency;

}
