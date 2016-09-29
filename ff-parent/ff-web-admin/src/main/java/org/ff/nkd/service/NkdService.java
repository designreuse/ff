package org.ff.nkd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Nkd;
import org.ff.jpa.repository.NkdRepository;
import org.ff.jpa.specification.NkdSpecification;
import org.ff.resource.nkd.NkdResource;
import org.ff.resource.nkd.NkdResourceAssembler;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NkdService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NkdRepository repository;

	@Autowired
	private NkdResourceAssembler resourceAssembler;

	@Cacheable("nkds")
	@Transactional(readOnly = true)
	public List<NkdResource> findAll() {
		log.debug("Finding NKDs...");
		return resourceAssembler.toResources(repository.findAll(), false);
	}

	@Transactional(readOnly = true)
	public NkdResource find(Integer id, Locale locale) {
		log.debug("Finding NKD [{}]...", id);

		if (id == 0) {
			return new NkdResource();
		}

		Nkd entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.nkd", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	@CacheEvict(value = "nkds", allEntries = true)
	public NkdResource save(NkdResource resource) {
		log.debug("Saving NKD [{}]...", resource);

		Nkd entity = null;
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
	@CacheEvict(value = "nkds", allEntries = true)
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting NKD [{}]...", id);

		Nkd entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.nkd", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<NkdResource> getPage(UiGridResource resource) {
		Page<Nkd> page = null;

		List<Specification<Nkd>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				NkdSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Nkd> specification = specifications.get(0);
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

	private NkdSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new NkdSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new NkdSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new NkdSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
