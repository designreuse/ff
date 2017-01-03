package org.ff.rest.investment.resource;

import java.util.Date;

import org.ff.jpa.domain.Investment.InvestmentStatus;
import org.ff.rest.image.resource.ImageResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class InvestmentResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("status")
	private InvestmentStatus status;

	@JsonProperty("name")
	private String name;

	@JsonProperty("text")
	private String text;

	@JsonProperty("image")
	private ImageResource image;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
