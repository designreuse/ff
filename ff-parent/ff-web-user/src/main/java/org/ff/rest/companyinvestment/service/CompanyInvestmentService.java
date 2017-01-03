package org.ff.rest.companyinvestment.service;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyInvestment;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.repository.CompanyInvestmentRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.companyinvestment.resource.CompanyInvestmentItemResource;
import org.ff.rest.companyinvestment.resource.CompanyInvestmentResource;
import org.ff.rest.companyinvestment.resource.CompanyInvestmentResourceAssembler;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyInvestmentService {

	@Autowired
	private CompanyInvestmentRepository companyInvestmentRepository;

	@Autowired
	private CompanyInvestmentResourceAssembler companyInvestmentResourceAssembler;

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
	public List<CompanyInvestmentResource> findAll(UserDetails principal) {
		return companyInvestmentResourceAssembler.toResources(companyInvestmentRepository.findByCompany(userRepository.findByEmail(principal.getUsername()).getCompany()), true);
	}

	@Transactional(readOnly = true)
	public CompanyInvestmentResource find(UserDetails principal, Integer id) {
		if (id == 0) {
			CompanyInvestmentResource resource = new CompanyInvestmentResource();
			Company company = userRepository.findByEmail(principal.getUsername()).getCompany();
			resource.setCompany(companyResourceAssembler.toResource(company, true));
			resource.setItems(new ArrayList<CompanyInvestmentItemResource>());
			for (Item item : itemRepository.findByEntityType(ItemEntityType.COMPANY)) {
				if (item.getMetaTag() != null && CompanyInvestmentResourceAssembler.getCompanyMetaTags().contains(item.getMetaTag())) {
					CompanyInvestmentItemResource companyInvestmentItemResource = new CompanyInvestmentItemResource();
					companyInvestmentItemResource.setItem(itemResourceAssembler.toResource(item, false));
					if (item.getMetaTag() == ItemMetaTag.COMPANY_INVESTMENT_AMOUNT) {
						companyInvestmentItemResource.setCurrency(currencyService.findAll().get(0));
					}
					resource.getItems().add(companyInvestmentItemResource);
				}
			}
			return resource;
		}
		return companyInvestmentResourceAssembler.toResource(companyInvestmentRepository.findOne(id), false);
	}

	@Transactional
	public CompanyInvestmentResource save(UserDetails principal, CompanyInvestmentResource resource) {
		CompanyInvestment entity = null;
		if (resource.getId() == null) {
			entity = companyInvestmentResourceAssembler.createEntity(resource);
		} else {
			entity = companyInvestmentResourceAssembler.updateEntity(companyInvestmentRepository.findOne(resource.getId()), resource);
		}
		entity = companyInvestmentRepository.save(entity);
		return companyInvestmentResourceAssembler.toResource(entity, false);
	}

	@Transactional
	public void delete(UserDetails principal, Integer id) {
		CompanyInvestment entity = companyInvestmentRepository.findOne(id);
		companyInvestmentRepository.delete(entity);
	}

}
