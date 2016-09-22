package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor @Getter @Setter @ToString
@Audited
@JsonInclude(Include.NON_NULL)
public class User extends AbstractEntity {

	public enum UserStatus { ACTIVE, INACTIVE };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer id;

	@Column(name = "status", nullable = false, columnDefinition="varchar(16)")
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(name = "first_name", nullable = false, columnDefinition = "varchar(128)")
	//	@Column(name = "first_name", nullable = false, columnDefinition = "nvarchar(128)")
	private String firstName;

	@Column(name = "last_name", nullable = false, columnDefinition = "varchar(128)")
	//	@Column(name = "last_name", nullable = false, columnDefinition = "nvarchar(128)")
	private String lastName;

	@Column(name = "email", nullable = false, columnDefinition = "varchar(256)")
	//	@Column(name = "email", nullable = false, columnDefinition = "nvarchar(256)")
	private String email;

	@Column(name = "password", nullable = false, columnDefinition = "varchar(128)")
	//	@Column(name = "password", nullable = false, columnDefinition = "nvarchar(128)")
	private String password;

}
