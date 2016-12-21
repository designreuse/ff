package org.ff.common.resource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractResource {

	@Getter @Setter
	@JsonProperty("creationDate")
	private Date creationDate;

	@Getter @Setter
	@JsonProperty("createdBy")
	private String createdBy;

	@Getter @Setter
	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@Getter @Setter
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
