package org.ff.rest.subdivision2.resource;

import java.util.Date;

import org.ff.rest.subdivision1.resource.Subdivision1Resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Subdivision2Resource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("subdivision1")
	private Subdivision1Resource subdivision1;

	@JsonProperty("developmentIndex")
	private String developmentIndex;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
