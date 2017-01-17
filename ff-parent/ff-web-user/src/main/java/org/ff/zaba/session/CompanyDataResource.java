package org.ff.zaba.session;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompanyDataResource {

	@JsonProperty("subjectName")
	private String subjectName;

	@JsonProperty("oibNumber")
	private String oibNumber;

	@JsonProperty("registrationNumber")
	private String registrationNumber;

	@JsonProperty("companyNumber")
	private String companyNumber;

	@JsonProperty("branchOfficeNumber")
	private String branchOfficeNumber;

	@JsonProperty("legalTypeNumber")
	private String legalTypeNumber;

	@JsonProperty("legalType")
	private String legalType;

	@JsonProperty("zipCode")
	private String zipCode;

	@JsonProperty("registrationCounty")
	private String registrationCounty;

	@JsonProperty("foundingDate")
	private Date foundingDate;

	@JsonProperty("primaryBusiness")
	private String primaryBusiness;

	@JsonProperty("otherBusiness")
	private List<String> otherBusiness;

	@JsonProperty("anyBankruptcyProcedure")
	private String anyBankruptcyProcedure;

	@JsonProperty("isBlockedFor5Days")
	private Boolean isBlockedFor5Days;

	@JsonProperty("isBlockedFor20Days")
	private Boolean isBlockedFor20Days;

	@JsonProperty("lastYearIncome")
	private String lastYearIncome;

	@JsonProperty("isProfitBeforeTax")
	private String isProfitBeforeTax;

	@JsonProperty("profitOrLoss")
	private String profitOrLoss;

	@JsonProperty("numberOfEmployeesAnnual")
	private String numberOfEmployeesAnnual;

	@JsonProperty("capital")
	private String capital;

	@JsonProperty("totalLiabilities")
	private String totalLiabilities;

}
