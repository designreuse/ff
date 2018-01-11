package org.ff.rest.company.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ProfileCompletenessResource {

	@JsonProperty("profileCompleteness")
	private Double profileCompleteness = new Double(0d);

	@JsonProperty("profileIncomplete")
	private Boolean profileIncomplete = Boolean.TRUE;

	@JsonProperty("projectsIncomplete")
	private Boolean projectsIncomplete = Boolean.FALSE;

}
