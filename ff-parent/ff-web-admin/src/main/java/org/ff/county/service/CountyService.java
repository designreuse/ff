package org.ff.county.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.County;
import org.ff.jpa.repository.CountyRepository;
import org.ff.jpa.specification.CountySpecification;
import org.ff.resource.county.CountyResource;
import org.ff.resource.county.CountyResourceAssembler;
import org.ff.service.BaseService;
import org.ff.uigrid.PageableResource;
import org.ff.uigrid.UiGridFilterResource;
import org.ff.uigrid.UiGridResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CountyService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CountyRepository repository;

	@Autowired
	private CountyResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Cacheable(value = "counties")
	@Transactional(readOnly = true)
	public List<CountyResource> findAll() {
		log.debug("Finding counties...");

		List<CountyResource> result = resourceAssembler.toResources(repository.findAll(), false);
		Collections.sort(result, new Comparator<CountyResource>() {
			@Override
			public int compare(CountyResource o1, CountyResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public CountyResource find(Integer id, Locale locale) {
		log.debug("Finding county [{}]...", id);

		if (id == 0) {
			return new CountyResource();
		}

		County entity = repository.findOne(id);
		if (entity == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.county", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}


	@Transactional
	@CacheEvict(value = "counties", allEntries = true)
	public CountyResource save(CountyResource resource) {
		log.debug("Saving county [{}]...", resource);

		County entity = null;
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
	@CacheEvict(value = "counties", allEntries = true)
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting county [{}]...", id);

		County entity = repository.findOne(id);
		if (entity == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.county", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<CountyResource> getPage(UiGridResource resource) {
		Page<County> page = null;

		List<Specification<County>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				CountySpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<County> specification = specifications.get(0);
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

	private CountySpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new CountySpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new CountySpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new CountySpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
