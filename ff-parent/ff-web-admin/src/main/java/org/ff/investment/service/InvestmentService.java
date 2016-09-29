package org.ff.investment.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.EnumUtils;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Investment.InvestmentStatus;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.jpa.specification.InvestmentSpecification;
import org.ff.resource.image.ImageResource;
import org.ff.resource.investment.InvestmentResource;
import org.ff.resource.investment.InvestmentResourceAssembler;
import org.ff.service.BaseService;
import org.ff.uigrid.PageableResource;
import org.ff.uigrid.UiGridFilterResource;
import org.ff.uigrid.UiGridResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvestmentService extends BaseService {

	@Autowired
	private MessageSource messageSource;

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

	@Transactional(readOnly = true)
	public InvestmentResource find(Integer id, Locale locale) {
		log.debug("Finding investment [{}]...", id);

		if (id == 0) {
			InvestmentResource resource = new InvestmentResource();
			resource.setImage(new ImageResource());
			return resource;
		}

		Investment entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.investment", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public InvestmentResource save(InvestmentResource resource) {
		log.debug("Saving investment [{}]...", resource);

		Investment entity = null;
		if (resource.getId() == null) {
			entity = resourceAssembler.createEntity(resource);
		} else {
			entity = repository.findOne(resource.getId());
			entity = resourceAssembler.updateEntity(entity, resource);
		}

		repository.save(entity);

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public InvestmentResource activate(Integer id, Locale locale) {
		log.debug("Activating investment [{}]...", id);

		Investment entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.investment", null, locale), id }, locale));
		}

		entity.setStatus(InvestmentStatus.ACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public InvestmentResource deactivate(Integer id, Locale locale) {
		log.debug("Deactivating investment [{}]...", id);

		Investment entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.investment", null, locale), id }, locale));
		}

		entity.setStatus(InvestmentStatus.INACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting investment [{}]...", id);

		Investment entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.investment", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<InvestmentResource> getPage(UiGridResource resource) {
		Page<Investment> page = null;

		List<Specification<Investment>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				InvestmentSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Investment> specification = specifications.get(0);
			if (specifications.size() > 1) {
				for (int i=1; i<specifications.size(); i++) {
					specification = Specifications.where(specification).and(specifications.get(i));
				}
			}
			page = repository.findAll(specification, createPageable(resource));
		} else {
			// no filtering
			page = repository.findAll(createPageable(resource));
		}

		return new PageableResource<>(page.getTotalElements(), resourceAssembler.toResources(page.getContent(), true));
	}

	private InvestmentSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new InvestmentSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("status")) {
			if (EnumUtils.isValidEnum(InvestmentStatus.class, resource.getTerm().toUpperCase())) {
				return new InvestmentSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, InvestmentStatus.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new InvestmentSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new InvestmentSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
