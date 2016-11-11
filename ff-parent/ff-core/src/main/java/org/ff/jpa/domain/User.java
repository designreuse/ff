package org.ff.jpa.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor @Getter @Setter @ToString
public class User extends AbstractEntity {

	public enum UserStatus { ACTIVE, INACTIVE, WAITING_CONFIRMATION };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 32)
	private UserStatus status;

	@Nationalized
	@Column(name = "first_name", nullable = false, length = 128)
	private String firstName;

	@Nationalized
	@Column(name = "last_name", nullable = false, length = 128)
	private String lastName;

	@Nationalized
	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Nationalized
	@Column(name = "password", nullable = false, length = 128)
	private String password;

	@ManyToOne
	@JoinColumn(name = "business_relationship_manager", nullable = true)
	private BusinessRelationshipManager businessRelationshipManager;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Company company;

	@Column(name = "registration_code", nullable = false, length = 255)
	private String registrationCode;

	@Column(name = "registration_code_sent", nullable = false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registrationCodeSentDate;

	@Column(name = "registration_code_confirmed", nullable = true)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registrationCodeConfirmedDate;

}
