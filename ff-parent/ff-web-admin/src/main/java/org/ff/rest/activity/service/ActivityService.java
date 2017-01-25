package org.ff.rest.activity.service;

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
import org.ff.jpa.domain.Activity;
import org.ff.jpa.repository.ActivityRepository;
import org.ff.jpa.specification.ActivitySpecification;
import org.ff.rest.activity.resource.ActivityResource;
import org.ff.rest.activity.resource.ActivityResourceAssembler;
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
public class ActivityService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ActivityRepository repository;

	@Autowired
	private ActivityResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<ActivityResource> findAll() {
		List<ActivityResource> result = resourceAssembler.toResources(repository.findAll(), false);

		Collections.sort(result, new Comparator<ActivityResource>() {
			@Override
			public int compare(ActivityResource o1, ActivityResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public ActivityResource find(Integer id, Locale locale) {
		if (id == 0) {
			return new ActivityResource();
		}

		Activity entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.activity", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}


	@Transactional
	public ActivityResource save(ActivityResource resource) {
		Activity entity = null;
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
	public void delete(Integer id, Locale locale) {
		Activity entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.activity", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<ActivityResource> getPage(UiGridResource resource) {
		Page<Activity> page = null;

		List<Specification<Activity>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				ActivitySpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Activity> specification = specifications.get(0);
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

	private ActivitySpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new ActivitySpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new ActivitySpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new ActivitySpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

	@Transactional(readOnly = true)
	public List<ActivityResource> exportActivities() {
		return resourceAssembler.toResources(repository.findAll(), false);
	}

	@Transactional
	public Integer importActivities(List<ActivityResource> resources) {
		int cntImported = 0;

		for (ActivityResource resource : resources) {
			log.debug("Importing {}", resources);

			Activity entity = resourceAssembler.createEntity(resource);
			repository.save(entity);

			log.debug("Activity [id: {}] successfully imported", entity.getId());

			cntImported++;
		}

		return cntImported;
	}

}
