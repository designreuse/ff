package org.ff.counters.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CountersResource {

	@JsonProperty("cntUsers")
	private Long cntUsers;

	@JsonProperty("cntTenders")
	private Long cntTenders;

	@JsonProperty("cntInvestments")
	private Long cntInvestments;

	@JsonProperty("cntArticles")
	private Long cntArticles;

}
