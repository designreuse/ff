package org.ff.rest.dashboard.resource;

import java.util.LinkedList;
import java.util.List;

import org.ff.rest.tender.resource.TenderResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DashboardResource {

	@JsonProperty("chartLabels")
	private List<String> chartLabels = new LinkedList<>();

	@JsonProperty("chartData")
	private List<Number> chartData = new LinkedList<>();

	@JsonProperty("tenders")
	private List<TenderResource> tenders = new LinkedList<>();

	@JsonProperty("cntTenders")
	private Integer cntTenders;

	@JsonProperty("cntProjects")
	private Integer cntProjects;

}
