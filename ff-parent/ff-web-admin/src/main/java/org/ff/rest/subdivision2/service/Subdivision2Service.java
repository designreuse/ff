package org.ff.rest.subdivision2.service;

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
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.jpa.specification.Subdivision2Specification;
import org.ff.rest.subdivision2.resource.Subdivision2Resource;
import org.ff.rest.subdivision2.resource.Subdivision2ResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Subdivision2Service extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Subdivision1Repository subdivision1Repository;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	@Autowired
	private Subdivision2ResourceAssembler subdivision2ResourceAssembler;

	@Autowired
	private Collator collator;

	@Cacheable("subdivisions2")
	@Transactional(readOnly = true)
	public List<Subdivision2Resource> findAll() {
		List<Subdivision2Resource> result = subdivision2ResourceAssembler.toResources(subdivision2Repository.findAll(), false);
		Collections.sort(result, new Comparator<Subdivision2Resource>() {
			@Override
			public int compare(Subdivision2Resource o1, Subdivision2Resource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public List<Subdivision2Resource> findAll4Subdivision1(String subdivision1Ids) {
		List<Subdivision2Resource> result = new ArrayList<>();

		for (String subdivision1Id : subdivision1Ids.split("\\|")) {
			result.addAll(subdivision2ResourceAssembler.toResources(subdivision2Repository.findBySubdivision1(subdivision1Repository.findOne(Integer.parseInt(subdivision1Id))), false));
		}

		Collections.sort(result, new Comparator<Subdivision2Resource>() {
			@Override
			public int compare(Subdivision2Resource o1, Subdivision2Resource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public Subdivision2Resource find(Integer id, Locale locale) {
		if (id == 0) {
			return new Subdivision2Resource();
		}

		Subdivision2 entity = subdivision2Repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.subdivision2", null, locale), id }, locale));
		}

		return subdivision2ResourceAssembler.toResource(entity, false);
	}

	@Transactional
	@CacheEvict(value = "subdivisions2", allEntries = true)
	public Subdivision2Resource save(Subdivision2Resource resource) {
		Subdivision2 entity = null;
		if (resource.getId() == null) {
			entity = subdivision2ResourceAssembler.createEntity(resource);
		} else {
			entity = subdivision2Repository.findOne(resource.getId());
			entity = subdivision2ResourceAssembler.updateEntity(entity, resource);
		}

		subdivision2Repository.save(entity);

		return subdivision2ResourceAssembler.toResource(entity, false);
	}

	@Transactional
	@CacheEvict(value = "subdivisions2", allEntries = true)
	public void delete(Integer id, Locale locale) {
		Subdivision2 entity = subdivision2Repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.subdivision2", null, locale), id }, locale));
		}

		subdivision2Repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<Subdivision2Resource> getPage(UiGridResource resource) {
		Page<Subdivision2> page = null;

		List<Specification<Subdivision2>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				Subdivision2Specification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Subdivision2> specification = specifications.get(0);
			if (specifications.size() > 1) {
				for (int i=1; i<specifications.size(); i++) {
					specification = Specifications.where(specification).and(specifications.get(i));
				}
			}
			page = subdivision2Repository.findAll(specification, createPageable(resource));
		} else {
			// no filtering
			page = subdivision2Repository.findAll(createPageable(resource));
		}

		return new PageableResource<>(page.getTotalElements(), subdivision2ResourceAssembler.toResources(page.getContent(), true));
	}

	private Subdivision2Specification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new Subdivision2Specification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new Subdivision2Specification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new Subdivision2Specification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
