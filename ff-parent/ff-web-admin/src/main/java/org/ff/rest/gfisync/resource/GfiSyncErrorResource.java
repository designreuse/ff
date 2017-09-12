package org.ff.rest.gfisync.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GfiSyncErrorResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("user")
	private String user;

	@JsonProperty("companyData")
	private String companyData;

	@JsonProperty("error")
	private String error;

}
