package org.ff.rest.debugging.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DebuggingResource {

	@JsonProperty("isMatch")
	private Boolean isMatch = Boolean.FALSE;

	@JsonProperty("entries")
	private List<DebuggingEntry> entries = new ArrayList<>();

	public void add(DebuggingEntry entry) {
		entries.add(entry);
	}

}
