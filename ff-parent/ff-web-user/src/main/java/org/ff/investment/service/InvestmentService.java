package org.ff.investment.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Investment.InvestmentStatus;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.investment.InvestmentResource;
import org.ff.resource.investment.InvestmentResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvestmentService {

	@Autowired
	private InvestmentRepository repository;

	@Autowired
	private InvestmentResourceAssembler resourceAssembler;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<InvestmentResource> findAll(UserDetails principal) {
		log.debug("Finding investments for user [{}]", principal.getUsername());

		List<InvestmentResource> resources = resourceAssembler.toResources(repository.findByStatus(InvestmentStatus.ACTIVE), false);

		User user = null;
		Company company = null;
		Set<Investment> investments = null;

		user = userRepository.findByEmail(principal.getUsername());
		if (user != null) {
			company = user.getCompany();
			if (company != null) {
				investments = company.getInvestments();
			} else {
				throw new RuntimeException(String.format("Company not found for user [%s]", principal.getUsername()));
			}
		} else {
			throw new RuntimeException(String.format("User [%s] not found", principal.getUsername()));
		}

		for (InvestmentResource resource : resources) {
			resource.setSelected(Boolean.FALSE);
			for (Investment investment : investments) {
				if (investment.getId().equals(resource.getId())) {
					resource.setSelected(Boolean.TRUE);
					break;
				}
			}
		}

		Collections.sort(resources, new Comparator<InvestmentResource>() {
			@Override
			public int compare(InvestmentResource o1, InvestmentResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return resources;
	}

	@Transactional
	public List<InvestmentResource> save(UserDetails principal, List<InvestmentResource> resources) {
		log.debug("Saving investments for user [{}]", principal.getUsername());

		User user = null;
		Company company = null;
		Set<Investment> investments = null;

		user = userRepository.findByEmail(principal.getUsername());
		if (user != null) {
			company = user.getCompany();
			if (company != null) {
				investments = company.getInvestments();
				if (investments == null) {
					investments = new HashSet<>();
				}
			} else {
				throw new RuntimeException(String.format("Company not found for user [%s]", principal.getUsername()));
			}
		} else {
			throw new RuntimeException(String.format("User [%s] not found", principal.getUsername()));
		}

		for (InvestmentResource resource : resources) {
			if (resource.getSelected() == Boolean.TRUE) {
				investments.add(repository.findOne(resource.getId()));
			} else {
				investments.remove(repository.findOne(resource.getId()));
			}
		}

		company.setInvestments(investments);
		companyRepository.save(company);

		return findAll(principal);
	}

}
