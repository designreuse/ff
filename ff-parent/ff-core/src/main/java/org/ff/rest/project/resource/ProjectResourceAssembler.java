package org.ff.rest.project.resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Project;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.image.resource.ImageResource;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.investment.resource.InvestmentResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectResourceAssembler {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private InvestmentResourceAssembler investmentResourceAssembler;

	@Autowired
	private ProjectItemResourceAssembler projectItemResourceAssembler;

	public ProjectResource toResource(Project entity, boolean light) {
		ProjectResource resource = new ProjectResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setDescription(entity.getDescription());
		resource.setImage(new ImageResource());
		resource.setCompany((entity.getCompany() != null) ? companyResourceAssembler.toResource(entity.getCompany(), light) : null);
		resource.setInvestments(investmentResourceAssembler.toResources(entity.getInvestments(), true));
		resource.setItems((entity.getItems() != null) ? projectItemResourceAssembler.toResources(entity.getItems(), light) : null);
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<ProjectResource> toResources(Iterable<Project> entities, boolean light) {
		List<ProjectResource> resources = new ArrayList<>();
		if (entities != null) {
			for (Project entity : entities) {
				resources.add(toResource(entity, light));
			}
		}
		return resources;
	}

	public Project createEntity(ProjectResource resource) {
		Project entity = new Project();
		entity.setName(resource.getName());
		entity.setDescription(resource.getDescription());
		entity.setCompany((resource.getCompany() != null) ? companyRepository.findOne(resource.getCompany().getId()) : null);

		if (entity.getInvestments() == null) {
			entity.setInvestments(new LinkedHashSet<Investment>());
		}
		entity.getInvestments().clear();

		if (resource.getInvestments() != null && !resource.getInvestments().isEmpty()) {
			for (InvestmentResource userResource : resource.getInvestments()) {
				entity.getInvestments().add(investmentRepository.findOne(userResource.getId()));
			}
		}

		entity.setItems(projectItemResourceAssembler.toEntities(resource.getItems(), entity));
		return entity;
	}

	public Project updateEntity(Project entity, ProjectResource resource) {
		entity.setName(resource.getName());
		entity.setDescription(resource.getDescription());
		entity.setCompany((resource.getCompany() != null) ? companyRepository.findOne(resource.getCompany().getId()) : null);

		if (entity.getInvestments() == null) {
			entity.setInvestments(new LinkedHashSet<Investment>());
		}
		entity.getInvestments().clear();

		if (resource.getInvestments() != null && !resource.getInvestments().isEmpty()) {
			for (InvestmentResource userResource : resource.getInvestments()) {
				entity.getInvestments().add(investmentRepository.findOne(userResource.getId()));
			}
		}

		entity.setItems(projectItemResourceAssembler.toEntities(resource.getItems(), entity));
		return entity;
	}

	public static List<ItemMetaTag> getCompanyInvestmentMetaTags() {
		List<ItemMetaTag> companyMetaTags = new ArrayList<>();
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_ACTIVITY);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_AMOUNT);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_FINANCING_TYPE);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION1);
		companyMetaTags.add(ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION2);
		return companyMetaTags;
	}

}
