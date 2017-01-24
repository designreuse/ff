package org.ff.rest.company.service;

import org.ff.jpa.domain.Company;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyResourceAssembler resourceAssembler;

	@Transactional(readOnly = true)
	public CompanyResource find(UserDetails principal) {
		CompanyResource resource = resourceAssembler.toResource(
				userRepository.findByEmail(principal.getUsername()).getCompany(), false);
		return resource;
	}

	@Transactional
	public CompanyResource save(UserDetails principal, CompanyResource resource) {
		Company entity = companyRepository.save(resourceAssembler.updateEntity(companyRepository.findOne(resource.getId()), resource));
		resource = resourceAssembler.toResource(entity, false);
		return resource;
	}

	@Transactional(readOnly = true)
	public Double validate(UserDetails principal) {
		if (principal != null) {
			CompanyResource resource = resourceAssembler.toResource(userRepository.findByEmail(principal.getUsername()).getCompany(), false);

			double cntMandatoryItems = 0;
			double cntEnteredItems = 0;

			for (ItemResource itemResource : resource.getItems()) {
				if (itemResource.getMetaTag() != null && ProjectResourceAssembler.getCompanyInvestmentMetaTags().contains(itemResource.getMetaTag())) {
					continue;
				}

				if (Boolean.TRUE == itemResource.getMandatory()) {
					cntMandatoryItems++;
					if (itemResource.getValue() != null) {
						cntEnteredItems++;
					}
				}
			}

			if (cntMandatoryItems == 0) {
				return 100d;
			} else if (cntMandatoryItems > 0 && cntEnteredItems == 0) {
				return 0d;
			} else {
				return (cntEnteredItems / cntMandatoryItems) * 100;
			}

		}

		return null;
	}

}
