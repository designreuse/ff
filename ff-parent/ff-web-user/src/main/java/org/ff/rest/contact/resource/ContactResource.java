package org.ff.rest.contact.resource;

import org.ff.common.resource.KeyValueResource;
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

	@JsonProperty("topic")
	private KeyValueResource topic;

	@JsonProperty("channel")
	private KeyValueResource channel;

	@JsonProperty("type")
	private KeyValueResource type;

	@JsonProperty("text")
	private String text;

}
