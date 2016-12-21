package org.ff.rest.businessrelationshipmanager.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.ff.base.service.BaseService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.BusinessRelationshipManager;
import org.ff.jpa.repository.BusinessRelationshipManagerRepository;
import org.ff.jpa.specification.BusinessRelationshipManagerSpecification;
import org.ff.rest.businessrelationshipmanager.resource.BusinessRelationshipManagerResource;
import org.ff.rest.businessrelationshipmanager.resource.BusinessRelationshipManagerResourceAssembler;
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
public class BusinessRelationshipManagerService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BusinessRelationshipManagerRepository repository;

	@Autowired
	private BusinessRelationshipManagerResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<BusinessRelationshipManagerResource> findAll() {
		log.debug("Finding business relationship managers...");

		List<BusinessRelationshipManagerResource> result = resourceAssembler.toResources(repository.findAll());
		Collections.sort(result, new Comparator<BusinessRelationshipManagerResource>() {
			@Override
			public int compare(BusinessRelationshipManagerResource o1, BusinessRelationshipManagerResource o2) {
				return collator.compare(o1.getLastName(), o2.getLastName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public BusinessRelationshipManagerResource find(Integer id, Locale locale) {
		log.debug("Finding business relationship manager [{}]...", id);

		if (id == 0) {
			return new BusinessRelationshipManagerResource();
		}

		BusinessRelationshipManager entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.businessRelationshipManager", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity);
	}

	@Transactional
	public BusinessRelationshipManagerResource save(BusinessRelationshipManagerResource resource) {
		log.debug("Saving business relationship manager [{}]...", resource);

		BusinessRelationshipManager entity = null;
		if (resource.getId() == null) {
			entity = resourceAssembler.createEntity(resource);
		} else {
			entity = repository.findOne(resource.getId());
			entity = resourceAssembler.updateEntity(entity, resource);
		}

		repository.save(entity);

		return resourceAssembler.toResource(entity);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting BusinessRelationshipManager [{}]...", id);

		BusinessRelationshipManager entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.businessRelationshipManager", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<BusinessRelationshipManagerResource> getPage(UiGridResource resource) {
		Page<BusinessRelationshipManager> page = null;

		List<Specification<BusinessRelationshipManager>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				BusinessRelationshipManagerSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<BusinessRelationshipManager> specification = specifications.get(0);
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

		return new PageableResource<>(page.getTotalElements(), resourceAssembler.toResources(page.getContent()));
	}

	private BusinessRelationshipManagerSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new BusinessRelationshipManagerSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new BusinessRelationshipManagerSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new BusinessRelationshipManagerSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
