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

	public enum UserRegistrationType { INTERNAL, EXTERNAL };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = true, length = 32)
	private UserStatus status;

	@Nationalized
	@Column(name = "first_name", nullable = true, length = 128)
	private String firstName;

	@Nationalized
	@Column(name = "last_name", nullable = true, length = 128)
	private String lastName;

	@Nationalized
	@Column(name = "email", nullable = true, length = 255)
	private String email;

	@Nationalized
	@Column(name = "password", nullable = true, length = 128)
	private String password;

	@ManyToOne
	@JoinColumn(name = "business_relationship_manager", nullable = true)
	private BusinessRelationshipManager businessRelationshipManager;

	@ManyToOne
	@JoinColumn(name = "business_relationship_manager_substitute", nullable = true)
	private BusinessRelationshipManager businessRelationshipManagerSubstitute;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Company company;

	@Column(name = "registration_code", nullable = true, length = 255)
	private String registrationCode;

	@Column(name = "registration_code_sent", nullable = true)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registrationCodeSentDate;

	@Column(name = "registration_code_confirmed", nullable = true)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registrationCodeConfirmedDate;

	@Column(name = "last_login", nullable = true)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime lastLoginDate;

	@Column(name = "demo_user", nullable = true)
	private Boolean demoUser;

	@Enumerated(EnumType.STRING)
	@Column(name = "registrationType", nullable = true, length = 32)
	private UserRegistrationType registrationType;

}
