package org.ff.rest.dashboard.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DashboardResource {

	@JsonProperty("usersRegistered")
	private Long usersRegistered;

	@JsonProperty("usersRegisteredInternal")
	private Long usersRegisteredInternal;

	@JsonProperty("usersRegisteredExternal")
	private Long usersRegisteredExternal;

	@JsonProperty("usersRegisteredInternalPercentage")
	private String usersRegisteredInternalPercentage;

	@JsonProperty("usersRegisteredExternalPercentage")
	private String usersRegisteredExternalPercentage;

	@JsonProperty("visitsTotal")
	private Long visitsTotal;

	@JsonProperty("visitsUnique")
	private Long visitsUnique;

	@JsonProperty("visitsUniquePercentage")
	private String visitsUniquePercentage;

	@JsonProperty("tenders")
	private Long tenders;

	@JsonProperty("tendersActive")
	private Long tendersActive;

	@JsonProperty("tendersInactive")
	private Long tendersInactive;

	@JsonProperty("tendersActivePercentage")
	private String tendersActivePercentage;

	@JsonProperty("tendersInactivePercentage")
	private String tendersInactivePercentage;

	@JsonProperty("projects")
	private Long projects;

	@JsonProperty("projectsValueTotal")
	private Double projectsValueTotal;

	@JsonProperty("projectsValueAvg")
	private Double projectsValueAvg;

	@JsonProperty("currency")
	private String currency;

}
