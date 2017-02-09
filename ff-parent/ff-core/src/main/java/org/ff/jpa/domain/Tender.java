package org.ff.jpa.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tender")
@NoArgsConstructor @Getter @Setter @ToString
public class Tender extends AbstractEntity {

	public enum TenderStatus { ACTIVE, INACTIVE };

	public enum TenderState { CLOSE, OPEN, PENDING };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private TenderStatus status;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Lob
	@Nationalized
	@Column(name = "text", nullable = true)
	private String text;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image", nullable = true)
	private Image image;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tender", orphanRemoval = true)
	@OrderBy("id")
	private Set<TenderItem> items;

	@Transient
	private Set<Project> projects = new HashSet<>();

}
