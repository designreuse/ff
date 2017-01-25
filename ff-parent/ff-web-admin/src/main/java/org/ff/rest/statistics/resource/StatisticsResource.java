package org.ff.rest.statistics.resource;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class StatisticsResource {

	public enum StatisticsType { COMPANIES_BY_COUNTIES, COMPANIES_BY_REVENUES, COMPANIES_BY_SIZE, INVESTMENTS_BY_COUNTIES, INVESTMENTS_BY_ACTIVITIES };

	@JsonProperty("type")
	private StatisticsType type;

	@JsonProperty("labels")
	private List<String> labels = new LinkedList<>();

	@JsonProperty("data")
	private List<Number> data = new LinkedList<>();

	@JsonProperty("data2")
	private List<Number> data2 = new LinkedList<>();

	@JsonProperty("timestamp")
	private Date timestamp;

	@JsonProperty("currency")
	private String currency;

}
