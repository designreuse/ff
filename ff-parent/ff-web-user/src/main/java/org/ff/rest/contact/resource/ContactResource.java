package org.ff.rest.contact.resource;

import org.ff.rest.company.resource.CompanyResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ContactResource {

	@JsonProperty("company")
	private CompanyResource company;

	@JsonProperty("name")
	private String name;

	@JsonProperty("email")
	private String email;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("location")
	private OfficeResource location;

	@JsonProperty("text")
	private String text;

}
