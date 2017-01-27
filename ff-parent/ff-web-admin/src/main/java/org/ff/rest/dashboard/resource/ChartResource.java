package org.ff.rest.dashboard.resource;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ChartResource {

	@JsonProperty("serie1")
	private LinkedList<LinkedList<Object>> serie1;

	@JsonProperty("serie2")
	private LinkedList<LinkedList<Object>> serie2;

}
