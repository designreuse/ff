package org.ff.rest.user.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GfiSyncReportResource {

	@JsonProperty("updateOK")
	List<UserResource> updateOK = new ArrayList<>();

	@JsonProperty("updateNOK")
	List<UserResource> updateNOK = new ArrayList<>();

}
