package org.ff.rest.company.service;

import org.apache.commons.lang3.StringUtils;
import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.ProjectRepository;
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

	@Autowired
	private ProjectRepository projectRepository;

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

			// projects incomplete
			if (projectRepository.countByCompany(userRepository.findOne(principal.getUser().getId()).getCompany()).intValue() == 0) {
				result.setProjectsIncomplete(Boolean.TRUE);
			}
		}

		return result;
	}

	@Transactional(readOnly = true)
	public Boolean getHideSyncDataWarning(AppUserDetails principal) {
		Boolean result = Boolean.TRUE;

		Company company = userRepository.findOne(principal.getUser().getId()).getCompany();
		if (company != null) {
			result = company.getHideSyncDataWarning();
		}

		return result;
	}

	@Transactional(readOnly = true)
	public Boolean showSyncDataWarningOrNot(AppUserDetails principal) {
		Boolean result = Boolean.FALSE;

		Company company = userRepository.findOne(principal.getUser().getId()).getCompany();
		if (Boolean.FALSE == company.getHideSyncDataWarning()) {
			for (CompanyItem item : company.getItems()) {
				if (StringUtils.isNotBlank(item.getValueExt()) && !item.getValueExt().equals(item.getValue())) {
					result = Boolean.TRUE;
					break;
				}
			}
		}

		return result;
	}

	@Transactional
	public void syncData(AppUserDetails principal, Integer option, Boolean hideSyncDataWarning) {
		Company company = userRepository.findOne(principal.getUser().getId()).getCompany();
		company.setHideSyncDataWarning(hideSyncDataWarning);

		if (option == 0) {
			// sync data now
			for (CompanyItem item : company.getItems()) {
				if (StringUtils.isNotBlank(item.getValueExt())) {
					item.setValue(item.getValueExt());
				}
			}
		} else if (option == 1) {
			// sync data now and in the future (automatically)
			company.setSyncData(Boolean.TRUE);
			for (CompanyItem item : company.getItems()) {
				if (StringUtils.isNotBlank(item.getValueExt())) {
					item.setValue(item.getValueExt());
				}
			}
		} else if (option == 2) {
			// do not sync data now nor in future
			company.setSyncData(Boolean.FALSE);
		}

		companyRepository.save(company);
	}

}
