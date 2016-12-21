package org.ff.rest.image.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ImageResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("base64")
	private String base64;

}
