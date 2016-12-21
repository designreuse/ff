package org.ff.rest.algorithmitem.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OperatorResource {

	@JsonProperty("value")
	private String value;

	@JsonProperty("name")
	private String name;

}
