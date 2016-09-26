package org.ff.investment.resource;

import java.util.Date;

import org.ff.image.resource.ImageResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class InvestmentResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("text")
	private String text;

	@JsonProperty("image")
	private ImageResource image;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("selected")
	private Boolean selected;

}
