package org.ff.jpa.envers;

import java.util.Date;

import org.hibernate.envers.RevisionType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class RevisionResource {

	@JsonProperty("clazz")
	private String clazz;

	@JsonProperty("entityId")
	private Integer entityId;

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("type")
	private RevisionType type;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

}
