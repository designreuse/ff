package org.ff.rest.organizationalunit.service;

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
import org.ff.jpa.domain.OrganizationalUnit;
import org.ff.jpa.repository.BusinessRelationshipManagerRepository;
import org.ff.jpa.repository.OrganizationalUnitRepository;
import org.ff.jpa.specification.OrganizationalUnitSpecification;
import org.ff.rest.organizationalunit.resource.OrganizationalUnitResource;
import org.ff.rest.organizationalunit.resource.OrganizationalUnitResourceAssembler;
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
public class OrganizationalUnitService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private OrganizationalUnitRepository repository;

	@Autowired
	private OrganizationalUnitResourceAssembler resourceAssembler;

	@Autowired
	private BusinessRelationshipManagerRepository businessRelationshipManagerRepository;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<OrganizationalUnitResource> findAll() {
		log.debug("Finding organizational units...");

		List<OrganizationalUnitResource> result = resourceAssembler.toResources(repository.findAll());
		Collections.sort(result, new Comparator<OrganizationalUnitResource>() {
			@Override
			public int compare(OrganizationalUnitResource o1, OrganizationalUnitResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public OrganizationalUnitResource find(Integer id, Locale locale) {
		log.debug("Finding organizational unit [{}]...", id);

		if (id == 0) {
			return new OrganizationalUnitResource();
		}

		OrganizationalUnit entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.organizationalUnit", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity);
	}

	@Transactional
	public OrganizationalUnitResource save(OrganizationalUnitResource resource) {
		log.debug("Saving organizational unit [{}]...", resource);

		OrganizationalUnit entity = null;
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
		log.debug("Deleting organizational unit [{}]...", id);

		OrganizationalUnit entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.organizationalUnit", null, locale), id }, locale));
		}

		businessRelationshipManagerRepository.nullifyOrganizationalUnit(entity);

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<OrganizationalUnitResource> getPage(UiGridResource resource) {
		Page<OrganizationalUnit> page = null;

		List<Specification<OrganizationalUnit>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				OrganizationalUnitSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<OrganizationalUnit> specification = specifications.get(0);
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

	private OrganizationalUnitSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new OrganizationalUnitSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new OrganizationalUnitSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new OrganizationalUnitSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

	@Transactional(readOnly = true)
	public List<OrganizationalUnitResource> exportRecords() {
		return resourceAssembler.toResources(repository.findAll());
	}

	@Transactional
	public Integer importRecords(List<OrganizationalUnitResource> resources) {
		int cntImported = 0;
		for (OrganizationalUnitResource resource : resources) {
			log.debug("Importing {}", resource);

			OrganizationalUnit entity = repository.findByCode(resource.getCode());

			if (entity != null) {
				log.debug("Skipping import of OrganizationalUnit [code: {}] as it already exists", resource.getCode());
				continue;
			}

			entity = new OrganizationalUnit();
			entity.setCode(resource.getCode());
			entity.setName(resource.getName());

			repository.save(entity);

			log.debug("OrganizationalUnit [id: {}, code: {}] successfully imported", entity.getId(), entity.getCode());
			cntImported++;
		}
		return cntImported;
	}

}
