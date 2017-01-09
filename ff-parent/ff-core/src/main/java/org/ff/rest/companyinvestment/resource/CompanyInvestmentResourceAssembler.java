package org.ff.rest.companyinvestment.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.CompanyInvestment;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.investment.resource.InvestmentResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyInvestmentResourceAssembler {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private InvestmentResourceAssembler investmentResourceAssembler;

	@Autowired
	private CompanyInvestmentItemResourceAssembler companyInvestmentItemResourceAssembler;

	public CompanyInvestmentResource toResource(CompanyInvestment entity, boolean light) {
		CompanyInvestmentResource resource = new CompanyInvestmentResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setDescription(entity.getDescription());
		resource.setCompany((entity.getCompany() != null) ? companyResourceAssembler.toResource(entity.getCompany(), light) : null);
		resource.setInvestment((entity.getInvestment() != null) ? investmentResourceAssembler.toResource(entity.getInvestment(), light) : null);
		resource.setItems((entity.getItems() != null) ? companyInvestmentItemResourceAssembler.toResources(entity.getItems(), light) : null);
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<CompanyInvestmentResource> toResources(Iterable<CompanyInvestment> entities, boolean light) {
		List<CompanyInvestmentResource> resources = new ArrayList<>();
		if (entities != null) {
			for (CompanyInvestment entity : entities) {
				resources.add(toResource(entity, light));
			}
		}
		return resources;
	}

	public CompanyInvestment createEntity(CompanyInvestmentResource resource) {
		CompanyInvestment entity = new CompanyInvestment();
		entity.setName(resource.getName());
		entity.setDescription(resource.getDescription());
		entity.setCompany((resource.getCompany() != null) ? companyRepository.findOne(resource.getCompany().getId()) : null);
		entity.setInvestment((resource.getInvestment() != null) ? investmentRepository.findOne(resource.getInvestment().getId()) : null);
		entity.setItems(companyInvestmentItemResourceAssembler.toEntities(resource.getItems(), entity));
		return entity;
	}

	public CompanyInvestment updateEntity(CompanyInvestment entity, CompanyInvestmentResource resource) {
		entity.setName(resource.getName());
		entity.setDescription(resource.getDescription());
		entity.setCompany((resource.getCompany() != null) ? companyRepository.findOne(resource.getCompany().getId()) : null);
		entity.setInvestment((resource.getInvestment() != null) ? investmentRepository.findOne(resource.getInvestment().getId()) : null);
		entity.setItems(companyInvestmentItemResourceAssembler.toEntities(resource.getItems(), entity));
		return entity;
	}

	public static List<ItemMetaTag> getCompanyMetaTags() {
		List<ItemMetaTag> companyMetaTags = new ArrayList<>();
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_ACTIVITY);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_AMOUNT);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_FINANCING_TYPE);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION1);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION2);
		return companyMetaTags;
	}

}
