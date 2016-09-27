package org.ff.resource.tender;

import java.util.Date;
import java.util.List;

import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.resource.image.ImageResource;
import org.ff.resource.item.ItemResource;

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

	@JsonProperty("name")
	private String name;

	@JsonProperty("text")
	private String text;

	@JsonProperty("image")
	private ImageResource image;

	@JsonProperty("items")
	private List<ItemResource> items;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
