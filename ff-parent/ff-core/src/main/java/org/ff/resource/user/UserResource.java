package org.ff.resource.user;

import java.util.Date;

import org.ff.jpa.domain.User.UserStatus;
import org.ff.resource.company.CompanyResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class UserResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("status")
	private UserStatus status;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("email")
	private String email;

	@JsonProperty("password")
	private String password;

	@JsonProperty("company")
	private CompanyResource company;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
