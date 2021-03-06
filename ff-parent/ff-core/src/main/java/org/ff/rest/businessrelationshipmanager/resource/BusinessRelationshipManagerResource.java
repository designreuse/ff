package org.ff.rest.businessrelationshipmanager.resource;

import java.util.Date;

import org.ff.rest.organizationalunit.resource.OrganizationalUnitResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class BusinessRelationshipManagerResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("email")
	private String email;

	@JsonProperty("organizationalUnit")
	private OrganizationalUnitResource organizationalUnit;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
