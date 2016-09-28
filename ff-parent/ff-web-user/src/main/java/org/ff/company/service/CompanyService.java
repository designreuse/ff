package org.ff.company.service;

import org.ff.jpa.domain.Company;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.company.CompanyResource;
import org.ff.resource.company.CompanyResourceAssembler;
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
		resource.setInvestments(null);
		return resource;
	}

	@Transactional
	public CompanyResource save(UserDetails principal, CompanyResource resource) {
		Company entity = companyRepository.save(resourceAssembler.updateEntity(companyRepository.findOne(resource.getId()), resource));
		resource = resourceAssembler.toResource(entity, false);
		resource.setInvestments(null);
		return resource;
	}

}
