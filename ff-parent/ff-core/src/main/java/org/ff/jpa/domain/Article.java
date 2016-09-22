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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
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
@Table(name = "article")
@NoArgsConstructor @Getter @Setter @ToString
@Audited
@JsonInclude(Include.NON_NULL)
public class Article extends AbstractEntity {

	public enum ArticleStatus { ACTIVE, INACTIVE };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer id;

	@Column(name = "status", nullable = false, columnDefinition="varchar(16)")
	@Enumerated(EnumType.STRING)
	private ArticleStatus status;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(512)")
	//	@Column(name = "name", nullable = false, columnDefinition = "nvarchar(512)")
	private String name;

	@Column(name = "text", nullable = true, columnDefinition = "longtext")
	//	@Column(name = "text", nullable = true, columnDefinition = "nvarchar(max)")
	private String text;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "image", nullable = true)
	@NotAudited @JsonIgnore
	private Image image;

}
