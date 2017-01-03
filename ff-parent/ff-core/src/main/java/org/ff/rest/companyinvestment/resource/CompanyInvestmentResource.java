package org.ff.rest.companyinvestment.resource;

import java.util.Date;
import java.util.List;

import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.investment.resource.InvestmentResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompanyInvestmentResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("company")
	private CompanyResource company;

	@JsonProperty("investment")
	private InvestmentResource investment;

	@JsonProperty("items")
	private List<CompanyInvestmentItemResource> items;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
