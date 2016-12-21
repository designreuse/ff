package org.ff.rest.usergroup.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.EnumUtils;
import org.ff.base.service.BaseService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.UserGroup;
import org.ff.jpa.domain.UserGroup.UserGroupStatus;
import org.ff.jpa.repository.UserGroupRepository;
import org.ff.jpa.specification.UserGroupSpecification;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.usergroup.resource.UserGroupResource;
import org.ff.rest.usergroup.resource.UserGroupResourceAssembler;
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
public class UserGroupService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserGroupRepository repository;

	@Autowired
	private UserGroupResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<UserGroupResource> findAll() {
		log.debug("Finding user groups...");

		List<UserGroupResource> result = resourceAssembler.toResources(repository.findByStatus(UserGroupStatus.ACTIVE), true);
		Collections.sort(result, new Comparator<UserGroupResource>() {
			@Override
			public int compare(UserGroupResource o1, UserGroupResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public UserGroupResource find(Integer id, Locale locale) {
		log.debug("Finding user group [{}]...", id);

		if (id == 0) {
			UserGroupResource resource = new UserGroupResource();
			resource.setUsers(new ArrayList<UserResource>());
			return resource;
		}

		UserGroup entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.userGroup", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public UserGroupResource save(UserGroupResource resource) {
		log.debug("Saving user group [{}]...", resource);

		UserGroup entity = null;
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
	public UserGroupResource activate(Integer id, Locale locale) {
		log.debug("Activating user group [{}]...", id);

		UserGroup entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.userGroup", null, locale), id }, locale));
		}

		entity.setStatus(UserGroupStatus.ACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public UserGroupResource deactivate(Integer id, Locale locale) {
		log.debug("Deactivating user [{}]...", id);

		UserGroup entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.userGroup", null, locale), id }, locale));
		}

		entity.setStatus(UserGroupStatus.INACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting user group [{}]...", id);

		UserGroup entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.userGroup", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<UserGroupResource> getPage(UiGridResource resource) {
		Page<UserGroup> page = null;

		List<Specification<UserGroup>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				UserGroupSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<UserGroup> specification = specifications.get(0);
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

	private UserGroupSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new UserGroupSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("status")) {
			if (EnumUtils.isValidEnum(UserGroupStatus.class, resource.getTerm().toUpperCase())) {
				return new UserGroupSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, UserGroupStatus.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new UserGroupSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new UserGroupSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
