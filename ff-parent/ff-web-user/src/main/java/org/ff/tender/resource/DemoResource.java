package org.ff.tender.resource;

import java.util.List;

import org.ff.resource.company.CompanyResource;
import org.ff.resource.investment.InvestmentResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DemoResource {

	@JsonProperty("company")
	private CompanyResource company;

	@JsonProperty("investments")
	private List<InvestmentResource> investments;

}
