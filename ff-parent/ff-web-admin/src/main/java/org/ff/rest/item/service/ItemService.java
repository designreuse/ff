package org.ff.rest.item.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.EnumUtils;
import org.ff.base.service.BaseService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.repository.AlgorithmItemRepository;
import org.ff.jpa.repository.CompanyItemRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.TenderItemRepository;
import org.ff.jpa.specification.ItemSpecification;
import org.ff.rest.item.resource.ItemOptionResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.resource.ItemResourceAssembler;
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
public class ItemService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ItemRepository repository;

	@Autowired
	private ItemResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Autowired
	private TenderItemRepository tenderItemRepository;

	@Autowired
	private CompanyItemRepository companyItemRepository;

	@Autowired
	private AlgorithmItemRepository algorithmItemRepository;

	@Transactional(readOnly = true)
	public List<ItemResource> findAll(ItemEntityType entityType) {
		log.debug("Finding items...");

		List<ItemResource> result = resourceAssembler.toResources(repository.findByEntityType(entityType), true);
		Collections.sort(result, new Comparator<ItemResource>() {
			@Override
			public int compare(ItemResource o1, ItemResource o2) {
				return collator.compare(o1.getCode(), o2.getCode());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public ItemResource find(String entityType, Integer id, Locale locale) {
		log.debug("Finding item [{}]...", id);

		if (id == 0) {
			ItemResource resource = new ItemResource();
			resource.setEntityType(ItemEntityType.valueOf(entityType.toUpperCase()));
			resource.setOptions(new ArrayList<ItemOptionResource>());
			resource.setMandatory(Boolean.FALSE);
			resource.setEmphasize(Boolean.FALSE);
			resource.setSummaryItem(Boolean.FALSE);
			resource.setWidgetItem(Boolean.FALSE);
			return resource;
		}

		Item entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.item", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public ItemResource save(ItemResource resource) {
		log.debug("Saving item [{}]...", resource);

		Item entity = null;
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
	public ItemResource activate(Integer id, Locale locale) {
		log.debug("Activating item [{}]...", id);

		Item entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.item", null, locale), id }, locale));
		}

		entity.setStatus(ItemStatus.ACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public ItemResource deactivate(Integer id, Locale locale) {
		log.debug("Deactivating item [{}]...", id);

		Item entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.item", null, locale), id }, locale));
		}

		entity.setStatus(ItemStatus.INACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting item [{}]...", id);

		Item entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.item", null, locale), id }, locale));
		}

		if (entity.getEntityType() == ItemEntityType.TENDER) {
			if (algorithmItemRepository.countByTenderItem(entity) > 0) {
				throw new RuntimeException(messageSource.getMessage("exception.itemDeleteNotAllowed", new Object[] { }, locale));
			}
			tenderItemRepository.deleteByItem(entity);
		} else if (entity.getEntityType() == ItemEntityType.COMPANY) {
			if (algorithmItemRepository.countByCompanyItem(entity) > 0) {
				throw new RuntimeException(messageSource.getMessage("exception.itemDeleteNotAllowed", new Object[] { }, locale));
			}
			companyItemRepository.deleteByItem(entity);
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<ItemResource> getPage(UiGridResource resource) {
		Page<Item> page = null;

		List<Specification<Item>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				ItemSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Item> specification = specifications.get(0);
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

	private ItemSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id") || resource.getName().equalsIgnoreCase("position")) {
			return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("status")) {
			if (EnumUtils.isValidEnum(ItemStatus.class, resource.getTerm().toUpperCase())) {
				return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, ItemStatus.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("entityType")) {
			if (EnumUtils.isValidEnum(ItemEntityType.class, resource.getTerm().toUpperCase())) {
				return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, ItemEntityType.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("type")) {
			if (EnumUtils.isValidEnum(ItemType.class, resource.getTerm().toUpperCase())) {
				return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, ItemType.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("mandatory") || resource.getName().equalsIgnoreCase("summaryItem") || resource.getName().equalsIgnoreCase("widgetItem")) {
			return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, Boolean.parseBoolean(resource.getTerm())));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new ItemSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

	public List<String> getMetatags(ItemEntityType entityType) {
		List<String> result = new ArrayList<>();
		for (ItemMetaTag itemMetaTag : ItemMetaTag.values()) {
			if (entityType == ItemEntityType.TENDER && itemMetaTag.name().startsWith("TENDER")) {
				result.add(itemMetaTag.name());
			} else if (entityType == ItemEntityType.COMPANY && itemMetaTag.name().startsWith("COMPANY")) {
				result.add(itemMetaTag.name());
			}
		}
		return result;
	}

	@Transactional(readOnly = true)
	public List<ItemResource> exportItems(ItemEntityType entityType) {
		return resourceAssembler.toResources(repository.findByEntityType(entityType), false);
	}

	@Transactional
	public Integer importItems(List<ItemResource> itemResources) {
		int cntImported = 0;
		for (ItemResource itemResource : itemResources) {
			log.debug("Importing {}", itemResource);

			Item item = repository.findByCodeAndEntityType(itemResource.getCode(), itemResource.getEntityType());

			if (item != null) {
				log.debug("Skipping import of item [code: {}] as it already exists", itemResource.getCode());
				continue;
			}

			item = new Item();
			item.setCode(itemResource.getCode());
			item.setEntityType(itemResource.getEntityType());
			item.setStatus((itemResource.getStatus() != null) ? itemResource.getStatus() : ItemStatus.INACTIVE);
			item.setType(itemResource.getType());
			item.setMetaTag(itemResource.getMetaTag());
			item.setSummaryItem((itemResource.getSummaryItem() != null) ? itemResource.getSummaryItem() : Boolean.FALSE);
			item.setWidgetItem((itemResource.getWidgetItem() != null) ? itemResource.getWidgetItem() : Boolean.FALSE);
			item.setMandatory((itemResource.getMandatory() != null) ? itemResource.getMandatory() : Boolean.FALSE);
			item.setPosition(itemResource.getPosition());
			item.setText(itemResource.getText());
			item.setHelp(itemResource.getHelp());

			if (itemResource.getOptions() != null) {
				item.setOptions(new HashSet<ItemOption>());
				for (ItemOptionResource itemOptionResource : itemResource.getOptions()) {
					ItemOption itemOption = new ItemOption();
					itemOption.setItem(item);
					itemOption.setPosition(itemOptionResource.getPosition());
					itemOption.setText(itemOptionResource.getText());
					item.getOptions().add(itemOption);
				}
			}

			repository.save(item);

			log.debug("Item [id: {}, code: {}] successfully imported", item.getId(), item.getCode());
			cntImported++;
		}
		return cntImported;
	}

}
