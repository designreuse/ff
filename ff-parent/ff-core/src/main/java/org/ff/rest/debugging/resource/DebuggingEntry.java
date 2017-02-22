package org.ff.rest.debugging.resource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DebuggingEntry {

	public enum Status { OK, NOK };

	@JsonProperty("date")
	private Date date;

	@JsonProperty("text")
	private String text;

	@JsonProperty("status")
	private Status status;

}
