package org.ff.algorithmitem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.EnumUtils;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.AlgorithmItem;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemStatus;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemType;
import org.ff.jpa.domain.AlgorithmItem.Operator;
import org.ff.jpa.repository.AlgorithmItemRepository;
import org.ff.jpa.specification.AlgorithmItemSpecification;
import org.ff.resource.algorithmitem.AlgorithmItemResource;
import org.ff.resource.algorithmitem.AlgorithmItemResourceAssembler;
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
public class AlgorithmItemService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AlgorithmItemRepository repository;

	@Autowired
	private AlgorithmItemResourceAssembler resourceAssembler;

	@Transactional(readOnly = true)
	public List<AlgorithmItemResource> findAll() {
		log.debug("Finding items...");

		List<AlgorithmItemResource> result = resourceAssembler.toResources(repository.findByStatusOrderByCode(AlgorithmItemStatus.ACTIVE), true);

		return result;
	}

	@Transactional(readOnly = true)
	public AlgorithmItemResource find(Integer id, Locale locale) {
		log.debug("Finding AlgorithmItem [{}]...", id);

		if (id == 0) {
			AlgorithmItemResource resource = new AlgorithmItemResource();
			return resource;
		}

		AlgorithmItem entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.algorithmItem", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public AlgorithmItemResource save(AlgorithmItemResource resource) {
		log.debug("Saving AlgorithmItem [{}]...", resource);

		AlgorithmItem entity = null;
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
	public AlgorithmItemResource activate(Integer id, Locale locale) {
		log.debug("Activating AlgorithmItem [{}]...", id);

		AlgorithmItem entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.algorithmItem", null, locale), id }, locale));
		}

		entity.setStatus(AlgorithmItemStatus.ACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public AlgorithmItemResource deactivate(Integer id, Locale locale) {
		log.debug("Deactivating AlgorithmItem [{}]...", id);

		AlgorithmItem entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.algorithmItem", null, locale), id }, locale));
		}

		entity.setStatus(AlgorithmItemStatus.INACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting AlgorithmItem [{}]...", id);

		AlgorithmItem entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.algorithmItem", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<AlgorithmItemResource> getPage(UiGridResource resource) {
		Page<AlgorithmItem> page = null;

		List<Specification<AlgorithmItem>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				AlgorithmItemSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<AlgorithmItem> specification = specifications.get(0);
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

	private AlgorithmItemSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new AlgorithmItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("operator")) {
			if (EnumUtils.isValidEnum(Operator.class, resource.getTerm().toUpperCase())) {
				return new AlgorithmItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, Operator.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("type")) {
			if (EnumUtils.isValidEnum(AlgorithmItemType.class, resource.getTerm().toUpperCase())) {
				return new AlgorithmItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, AlgorithmItemType.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("status")) {
			if (EnumUtils.isValidEnum(AlgorithmItemStatus.class, resource.getTerm().toUpperCase())) {
				return new AlgorithmItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, AlgorithmItemStatus.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new AlgorithmItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new AlgorithmItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
