package org.ff.rest.error.resource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ErrorResource {

	@JsonProperty("timestamp")
	public Date timestamp;

	@JsonProperty("status")
	public Integer status;

	@JsonProperty("error")
	public String error;

	@JsonProperty("message")
	public String message;

	@JsonProperty("path")
	public String path;

	@JsonProperty("trace")
	public String trace;

}
