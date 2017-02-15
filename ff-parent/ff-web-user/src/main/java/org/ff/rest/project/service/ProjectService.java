package org.ff.rest.project.service;

import java.util.ArrayList;
import java.util.List;

import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Project;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.project.resource.ProjectItemResource;
import org.ff.rest.project.resource.ProjectResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectResourceAssembler projectResourceAssembler;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public List<ProjectResource> findAll(AppUserDetails principal) {
		return projectResourceAssembler.toResources(projectRepository.findByCompany(userRepository.findOne(principal.getUser().getId()).getCompany()), true);
	}

	@Transactional(readOnly = true)
	public ProjectResource find(AppUserDetails principal, Integer id) {
		if (id == 0) {
			ProjectResource resource = new ProjectResource();
			Company company = userRepository.findOne(principal.getUser().getId()).getCompany();
			resource.setCompany(companyResourceAssembler.toResource(company, true));
			resource.setItems(new ArrayList<ProjectItemResource>());
			for (Item item : itemRepository.findByEntityType(ItemEntityType.COMPANY)) {
				if (item.getMetaTag() != null && ProjectResourceAssembler.getCompanyInvestmentMetaTags().contains(item.getMetaTag())) {
					ProjectItemResource projectItemResource = new ProjectItemResource();
					projectItemResource.setItem(itemResourceAssembler.toResource(item, false));
					if (item.getMetaTag() == ItemMetaTag.COMPANY_INVESTMENT_AMOUNT) {
						projectItemResource.setCurrency(currencyService.findAll().get(0));
					}
					resource.getItems().add(projectItemResource);
				}
			}
			return resource;
		}
		return projectResourceAssembler.toResource(projectRepository.findOne(id), false);
	}

	@Transactional
	public ProjectResource save(AppUserDetails principal, ProjectResource resource) {
		Project entity = null;
		if (resource.getId() == null) {
			entity = projectResourceAssembler.createEntity(resource);
		} else {
			entity = projectResourceAssembler.updateEntity(projectRepository.findOne(resource.getId()), resource);
		}
		entity = projectRepository.save(entity);
		return projectResourceAssembler.toResource(entity, false);
	}

	@Transactional
	public void delete(AppUserDetails principal, Integer id) {
		Project entity = projectRepository.findOne(id);
		projectRepository.delete(entity);
	}

}
