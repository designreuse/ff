package org.ff.rest.contact.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class OfficeResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("subdivision1")
	private String subdivision1;

	@JsonProperty("subdivision2")
	private String subdivision2;

	@JsonProperty("zipCode")
	private String zipCode;

	@JsonProperty("address")
	private String address;

	@JsonProperty("prefix")
	private String prefix;

}
