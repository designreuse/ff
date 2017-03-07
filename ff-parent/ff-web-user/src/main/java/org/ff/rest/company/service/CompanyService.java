package org.ff.rest.company.service;

import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Company;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.company.resource.ProfileCompletenessResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
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
	public CompanyResource find(AppUserDetails principal) {
		CompanyResource resource = resourceAssembler.toResource(
				userRepository.findOne(principal.getUser().getId()).getCompany(), false);
		return resource;
	}

	@Transactional
	public CompanyResource save(AppUserDetails principal, CompanyResource resource) {
		Company entity = companyRepository.save(resourceAssembler.updateEntity(companyRepository.findOne(resource.getId()), resource));
		resource = resourceAssembler.toResource(entity, false);
		return resource;
	}

	@Transactional(readOnly = true)
	public ProfileCompletenessResource profileCompleteness(AppUserDetails principal) {
		ProfileCompletenessResource result = new ProfileCompletenessResource();

		if (principal != null) {
			double cntTotal = 0;
			double cntTotalFilled = 0;
			double cntMandatory = 0;
			double cntMandatoryFilled = 0;

			for (ItemResource itemResource : resourceAssembler.toResource(userRepository.findOne(principal.getUser().getId()).getCompany(), false).getItems()) {
				if (itemResource.getMetaTag() != null && ProjectResourceAssembler.getCompanyInvestmentMetaTags().contains(itemResource.getMetaTag())) {
					continue;
				}

				cntTotal++;
				if (itemResource.getValue() != null) {
					cntTotalFilled++;
				}

				if (Boolean.TRUE == itemResource.getMandatory()) {
					cntMandatory++;
					if (itemResource.getValue() != null) {
						cntMandatoryFilled++;
					}
				}
			}

			if (cntTotalFilled == 0) {
				result.setProfileCompleteness(0d);
			} else {
				result.setProfileCompleteness((cntTotalFilled / cntTotal) * 100);
			}

			if (cntMandatory == cntMandatoryFilled) {
				result.setProfileIncomplete(Boolean.FALSE);
			} else {
				result.setProfileIncomplete(Boolean.TRUE);
			}
		}

		return result;
	}

}
