package org.ff.rest.project.resource;

import java.util.Date;
import java.util.List;

import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.image.resource.ImageResource;
import org.ff.rest.investment.resource.InvestmentResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ProjectResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("image")
	private ImageResource image;

	@JsonProperty("company")
	private CompanyResource company;

	@JsonProperty("investments")
	private List<InvestmentResource> investments;

	@JsonProperty("items")
	private List<ProjectItemResource> items;

	@JsonProperty("matchingTenders")
	private List<String> matchingTenders;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
