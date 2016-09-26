package org.ff.jpa.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "investment")
@NoArgsConstructor @Getter @Setter @ToString
@Audited
@JsonInclude(Include.NON_NULL)
public class Investment extends AbstractEntity {

	public enum InvestmentStatus { ACTIVE, INACTIVE };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private InvestmentStatus status;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Lob
	@Nationalized
	@Column(name = "text", nullable = true)
	private String text;

	@NotAudited
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "image", nullable = true)
	@JsonIgnore
	private Image image;

}
