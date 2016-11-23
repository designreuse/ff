package org.ff.role.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Role;
import org.ff.jpa.repository.RoleRepository;
import org.ff.jpa.specification.RoleSpecification;
import org.ff.resource.permission.PermissionResource;
import org.ff.resource.role.RoleResource;
import org.ff.resource.role.RoleResourceAssembler;
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
public class RoleService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private RoleRepository repository;

	@Autowired
	private RoleResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<RoleResource> findAll() {
		log.debug("Finding roles...");

		List<RoleResource> result = resourceAssembler.toResources(repository.findAll(), true);
		Collections.sort(result, new Comparator<RoleResource>() {
			@Override
			public int compare(RoleResource o1, RoleResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public RoleResource find(Integer id, Locale locale) {
		log.debug("Finding role [{}]...", id);

		if (id == 0) {
			RoleResource resource = new RoleResource();
			resource.setPermissions(new ArrayList<PermissionResource>());
			return resource;
		}

		Role entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.role", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public RoleResource save(RoleResource resource) {
		log.debug("Saving role [{}]...", resource);

		Role entity = null;
		if (resource.getId() == null) {
			entity = resourceAssembler.createEntity(resource);
		} else {
			entity = repository.findOne(resource.getId());
			entity = resourceAssembler.updateEntity(entity, resource);
		}

		repository.save(entity);
		resource = resourceAssembler.toResource(entity, false);

		return resource;
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting role [{}]...", id);

		Role entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.role", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<RoleResource> getPage(UiGridResource resource) {
		Page<Role> page = null;

		List<Specification<Role>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				RoleSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Role> specification = specifications.get(0);
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

	private RoleSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new RoleSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new RoleSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new RoleSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
