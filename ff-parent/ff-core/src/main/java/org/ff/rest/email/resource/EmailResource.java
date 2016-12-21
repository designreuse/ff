package org.ff.rest.email.resource;

import org.ff.common.resource.AbstractResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class EmailResource extends AbstractResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("text")
	private String text;

}
