package org.ff.rest.investment.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.jpa.domain.Investment.InvestmentStatus;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.investment.resource.InvestmentResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvestmentService {

	@Autowired
	private InvestmentRepository repository;

	@Autowired
	private InvestmentResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<InvestmentResource> findAll() {
		List<InvestmentResource> result = resourceAssembler.toResources(repository.findByStatus(InvestmentStatus.ACTIVE), true);
		Collections.sort(result, new Comparator<InvestmentResource>() {
			@Override
			public int compare(InvestmentResource o1, InvestmentResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

}
