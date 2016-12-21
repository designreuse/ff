package org.ff.sse.resource;

import org.ff.rest.counters.resource.CountersResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SseResource {

	public enum SseType { COUNTERS_UPDATE };

	@JsonProperty("type")
	private SseType type;

	@JsonProperty("counters")
	private CountersResource counters;

}
