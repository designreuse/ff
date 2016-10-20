package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "user_email")
@NoArgsConstructor @Getter @Setter @ToString
public class UserEmail extends AbstractEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user", nullable = true)
	private User user;

	@ManyToOne
	@JoinColumn(name = "email", nullable = true)
	private Email email;

	@ManyToOne
	@JoinColumn(name = "tender", nullable = true)
	private Tender tender;

}
