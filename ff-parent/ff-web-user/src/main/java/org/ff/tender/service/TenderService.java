package org.ff.tender.service;

import java.util.List;

import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.tender.TenderResource;
import org.ff.resource.tender.TenderResourceAssembler;
import org.ff.service.algorithm.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlgorithmService algorithmService;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private ImpressionRepository impressionRepository;

	@Transactional(readOnly = true)
	public List<TenderResource> findAll(UserDetails principal) {
		List<Tender> tenders = algorithmService.findTenders4User(userRepository.findByEmail(principal.getUsername()));
		return tenderResourceAssembler.toResources(tenders, false);
	}

	@Transactional
	public TenderResource find(Integer id) {
		Tender entity = tenderRepository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(String.format("Tender [%s] not found", id));
		}

		Impression impression = new Impression();
		impression.setEntityType(EntityType.TENDER);
		impression.setEntityId(entity.getId());
		impressionRepository.save(impression);

		return tenderResourceAssembler.toResource(entity, false);
	}

}
