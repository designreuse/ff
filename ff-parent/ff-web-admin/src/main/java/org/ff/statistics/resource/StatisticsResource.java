package org.ff.statistics.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class StatisticsResource {

	public enum StatisticsType { COMPANIES_BY_COUNTIES, COMPANIES_BY_INVESTMENTS, COMPANIES_BY_REVENUES, COMPANIES_BY_SECTORS };

	@JsonProperty("type")
	private StatisticsType type;

	@JsonProperty("labels")
	private List<String> labels = new ArrayList<>();

	@JsonProperty("data")
	private List<Number> data = new ArrayList<>();

	@JsonProperty("timestamp")
	private Date timestamp;

}
