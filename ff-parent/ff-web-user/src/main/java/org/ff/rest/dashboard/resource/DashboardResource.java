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

	@JsonProperty("chartDataSeria1")
	private List<Number> chartDataSeria1 = new LinkedList<>();

	@JsonProperty("chartDataSeria2")
	private List<Double> chartDataSeria2 = new LinkedList<>();

	@JsonProperty("tenders")
	private List<TenderResource> tenders = new LinkedList<>();

	@JsonProperty("cntTenders")
	private Integer cntTenders;

	@JsonProperty("cntTendersOpen")
	private Integer cntTendersOpen;

	@JsonProperty("cntTenders4U")
	private Integer cntTenders4U;

	@JsonProperty("cntProjects")
	private Integer cntProjects;

	@JsonProperty("cntArticles")
	private Integer cntArticles;

}
