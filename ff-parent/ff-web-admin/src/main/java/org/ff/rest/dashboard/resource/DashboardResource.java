package org.ff.rest.dashboard.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DashboardResource {

	@JsonProperty("usersRegistered")
	private Integer usersRegistered;

	@JsonProperty("usersRegisteredInternal")
	private Integer usersRegisteredInternal;

	@JsonProperty("usersRegisteredExternal")
	private Integer usersRegisteredExternal;

	@JsonProperty("usersRegisteredInternalPercentage")
	private String usersRegisteredInternalPercentage;

	@JsonProperty("usersRegisteredExternalPercentage")
	private String usersRegisteredExternalPercentage;

}
