package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "algorithm_item")
@NoArgsConstructor @Getter @Setter @ToString
public class AlgorithmItem extends AbstractEntity {

	public enum AlgorithmItemType { MANDATORY, OPTIONAL, CONDITIONAL };
	public enum AlgorithmItemStatus { ACTIVE, INACTIVE };
	public enum Operator { EQUAL, GREATER_OR_EQUAL, LESS_OR_EQUAL, IN };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "code", nullable = false, length = 8)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 16)
	private AlgorithmItemType type;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private AlgorithmItemStatus status;

	@ManyToOne
	@JoinColumn(name = "company_item")
	private Item companyItem;

	@ManyToOne
	@JoinColumn(name = "tender_item")
	private Item tenderItem;

	@Enumerated(EnumType.STRING)
	@Column(name = "operator", nullable = false, length = 16)
	private Operator operator;

	@Column(name = "conditional_item_code", nullable = true, length = 8)
	private String conditionalItemCode;

}
