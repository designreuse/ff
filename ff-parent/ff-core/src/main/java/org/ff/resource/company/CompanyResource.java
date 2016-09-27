package org.ff.resource.company;

import java.util.Date;
import java.util.List;

import org.ff.resource.investment.InvestmentResource;
import org.ff.resource.item.ItemResource;
import org.ff.resource.tender.TenderResource;

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

	@JsonProperty("investments")
	private List<InvestmentResource> investments;

	@JsonProperty("tenders")
	private List<TenderResource> tenders;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
