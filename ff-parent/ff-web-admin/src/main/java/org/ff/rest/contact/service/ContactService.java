package org.ff.rest.contact.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ff.base.service.BaseService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Contact;
import org.ff.jpa.repository.ContactRepository;
import org.ff.jpa.specification.ContactSpecification;
import org.ff.rest.contact.resource.ContactResource;
import org.ff.rest.contact.resource.ContactResourceAssembler;
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
public class ContactService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ContactRepository repository;

	@Autowired
	private ContactResourceAssembler resourceAssembler;

	@Transactional(readOnly = true)
	public ContactResource find(Integer id, Locale locale) {
		log.debug("Finding contact [{}]...", id);

		if (id == 0) {
			return new ContactResource();
		}

		Contact entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.contact", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting contact [{}]...", id);

		Contact entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.contact", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<ContactResource> getPage(UiGridResource resource) {
		Page<Contact> page = null;

		List<Specification<Contact>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				ContactSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Contact> specification = specifications.get(0);
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

	private ContactSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new ContactSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new ContactSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new ContactSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

}
