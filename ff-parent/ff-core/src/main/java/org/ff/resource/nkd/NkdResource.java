package org.ff.resource.nkd;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class NkdResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("sector")
	private String sector;

	@JsonProperty("sectorName")
	private String sectorName;

	@JsonProperty("area")
	private String area;

	@JsonProperty("activity")
	private String activity;

	@JsonProperty("activityName")
	private String activityName;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
