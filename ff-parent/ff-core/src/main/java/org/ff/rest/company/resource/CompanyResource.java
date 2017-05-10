package org.ff.rest.company.resource;

import java.util.Date;
import java.util.List;

import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.tender.resource.TenderResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompanyResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("items")
	private List<ItemResource> items;

	@JsonProperty("tenders")
	private List<TenderResource> tenders;

	@JsonProperty("syncData")
	private Boolean syncData;

	@JsonProperty("hideSyncDataWarning")
	private Boolean hideSyncDataWarning;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
