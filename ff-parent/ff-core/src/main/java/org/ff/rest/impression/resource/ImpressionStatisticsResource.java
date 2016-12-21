package org.ff.rest.impression.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ImpressionStatisticsResource {

	@JsonProperty("name")
	private String name;

	@JsonProperty("total")
	private Long total;

	@JsonProperty("unique")
	private Long unique;

	@JsonProperty("labels")
	private List<String> labels = new ArrayList<>();

	@JsonProperty("series")
	private List<String> series = new ArrayList<>();

	@JsonProperty("data")
	private List<List<Number>> data = new ArrayList<>();

}
