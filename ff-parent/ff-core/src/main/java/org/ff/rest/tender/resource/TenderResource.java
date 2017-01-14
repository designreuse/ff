package org.ff.rest.tender.resource;

import java.util.Date;
import java.util.List;

import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.rest.image.resource.ImageResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.project.resource.ProjectResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TenderResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("status")
	private TenderStatus status;

	@JsonProperty("incomplete")
	private Boolean incomplete;

	@JsonProperty("name")
	private String name;

	@JsonProperty("text")
	private String text;

	@JsonProperty("imageId")
	private Integer imageId;

	@JsonProperty("image")
	private ImageResource image;

	@JsonProperty("items")
	private List<ItemResource> items;

	@JsonProperty("projects")
	private List<ProjectResource> projects;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
