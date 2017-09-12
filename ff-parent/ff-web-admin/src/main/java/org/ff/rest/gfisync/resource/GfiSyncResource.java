package org.ff.rest.gfisync.resource;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GfiSyncResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("cntTotal")
	private Integer cntTotal;

	@JsonProperty("cntOk")
	private Integer cntOk;

	@JsonProperty("cntNok")
	private Integer cntNok;

	@JsonProperty("startTime")
	private Date startTime;

	@JsonProperty("endTime")
	private Date endTime;

	@JsonProperty("errors")
	private List<GfiSyncErrorResource> errors;

}
