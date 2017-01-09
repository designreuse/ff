package org.ff.rest.companyinvestment.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Activity;
import org.ff.jpa.domain.CompanyInvestment;
import org.ff.jpa.domain.CompanyInvestmentItem;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.ActivityRepository;
import org.ff.jpa.repository.CompanyInvestmentItemRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.rest.activity.resource.ActivityResource;
import org.ff.rest.activity.resource.ActivityResourceAssembler;
import org.ff.rest.currency.resource.CurrencyResource;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.item.resource.ItemOptionResource;
import org.ff.rest.item.resource.ItemOptionResourceAssembler;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.subdivision1.resource.Subdivision1Resource;
import org.ff.rest.subdivision1.resource.Subdivision1ResourceAssembler;
import org.ff.rest.subdivision2.resource.Subdivision2Resource;
import org.ff.rest.subdivision2.resource.Subdivision2ResourceAssembler;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CompanyInvestmentItemResourceAssembler {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	@Autowired
	private ItemOptionResourceAssembler itemOptionResourceAssembler;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private ActivityResourceAssembler activityResourceAssembler;

	@Autowired
	private Subdivision1Repository subdivision1Repository;

	@Autowired
	private Subdivision1ResourceAssembler subdivision1ResourceAssembler;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	@Autowired
	private Subdivision2ResourceAssembler subdivision2ResourceAssembler;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private CompanyInvestmentItemRepository companyInvestmentItemRepository;

	public CompanyInvestmentItemResource toResource(CompanyInvestmentItem entity, boolean light) {
		CompanyInvestmentItemResource resource = new CompanyInvestmentItemResource();
		resource.setId(entity.getId());
		resource.setItem((entity.getItem() != null) ? itemResourceAssembler.toResource(entity.getItem(), light) : null);
		resource.setValue(entity.getValue());
		resource.setCurrency(new CurrencyResource(entity.getCurrency()));
		setResourceValue(resource, entity);
		return resource;
	}

	public List<CompanyInvestmentItemResource> toResources(Iterable<CompanyInvestmentItem> entities, boolean light) {
		List<CompanyInvestmentItemResource> resources = new ArrayList<>();
		for (CompanyInvestmentItem entity : entities) {
			resources.add(toResource(entity, light));
		}

		Collections.sort(resources, new Comparator<CompanyInvestmentItemResource>() {
			@Override
			public int compare(CompanyInvestmentItemResource o1, CompanyInvestmentItemResource o2) {
				return o1.getItem().getPosition().compareTo(o2.getItem().getPosition());
			}
		});

		return resources;
	}

	public CompanyInvestmentItem toEntity(CompanyInvestmentItemResource resource, CompanyInvestment companyInvestment) {
		CompanyInvestmentItem entity = (resource.getId() == null) ? new CompanyInvestmentItem() : companyInvestmentItemRepository.findOne(resource.getId());
		entity.setCompanyInvestment(companyInvestment);
		entity.setItem(itemRepository.findOne(resource.getItem().getId()));
		entity.setCurrency((resource.getCurrency() != null) ? resource.getCurrency().getCode() : null);
		setEntityValue(resource, entity);

		return entity;
	}

	public Set<CompanyInvestmentItem> toEntities(List<CompanyInvestmentItemResource> resources, CompanyInvestment entity) {
		Set<CompanyInvestmentItem> entities = (entity.getItems() != null) ? entity.getItems() : new HashSet<CompanyInvestmentItem>();
		for (CompanyInvestmentItemResource resource : resources) {
			entities.add(toEntity(resource, entity));
		}
		return entities;
	}

	private void setResourceValue(CompanyInvestmentItemResource itemResource, CompanyInvestmentItem companyInvestmentItem) {
		try {
			if (itemResource.getItem().getType() == ItemType.NUMBER) {
				itemResource.setValue((companyInvestmentItem.getValue() != null) ? Integer.parseInt(companyInvestmentItem.getValue()) : null);
			} else if (itemResource.getItem().getType() == ItemType.CURRENCY) {
				itemResource.setValue((companyInvestmentItem.getValue() != null) ? Integer.parseInt(companyInvestmentItem.getValue()) : null);
				if (itemResource.getItem().getType() == ItemType.CURRENCY) {
					if (StringUtils.isNotBlank(companyInvestmentItem.getCurrency())) {
						itemResource.setCurrency(new CurrencyResource(companyInvestmentItem.getCurrency()));
					} else {
						itemResource.setCurrency(new CurrencyResource(currencyService.findAll().get(0).getCode()));
					}
				}
			} else if (itemResource.getItem().getType() == ItemType.DATE) {
				itemResource.setValue(companyInvestmentItem.getValue());
			} else if (itemResource.getItem().getType() == ItemType.RADIO) {
				itemResource.setValue(Integer.parseInt(companyInvestmentItem.getValue()));
			} else if (itemResource.getItem().getType() == ItemType.SELECT) {
				if (StringUtils.isNotBlank(companyInvestmentItem.getValue())) {
					ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(companyInvestmentItem.getValue()));
					itemResource.setValue(itemOptionResourceAssembler.toResource(itemOption, true));
				}
			} else if (itemResource.getItem().getType() == ItemType.ACTIVITY) {
				if (StringUtils.isNotBlank(companyInvestmentItem.getValue())) {
					Activity entity = activityRepository.findOne(Integer.parseInt(companyInvestmentItem.getValue()));
					itemResource.setValue(activityResourceAssembler.toResource(entity, true));
				}
			} else if (itemResource.getItem().getType() == ItemType.SUBDIVISION1) {
				if (StringUtils.isNotBlank(companyInvestmentItem.getValue())) {
					Subdivision1 entity = subdivision1Repository.findOne(Integer.parseInt(companyInvestmentItem.getValue()));
					itemResource.setValue(subdivision1ResourceAssembler.toResource(entity, true));
				}
			} else if (itemResource.getItem().getType() == ItemType.SUBDIVISION2) {
				if (StringUtils.isNotBlank(companyInvestmentItem.getValue())) {
					Subdivision2 entity = subdivision2Repository.findOne(Integer.parseInt(companyInvestmentItem.getValue()));
					itemResource.setValue(subdivision2ResourceAssembler.toResource(entity, true));
				}
			} else {
				itemResource.setValue(companyInvestmentItem.getValue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void setEntityValue(CompanyInvestmentItemResource companyInvestmentItemResource, CompanyInvestmentItem companyInvestmentItem) {
		String value = null;

		try {
			if (companyInvestmentItemResource.getValue() != null) {
				if (companyInvestmentItemResource.getItem().getType() == ItemType.TEXT || companyInvestmentItemResource.getItem().getType() == ItemType.TEXT_AREA || companyInvestmentItemResource.getItem().getType() == ItemType.NUMBER) {
					value = companyInvestmentItemResource.getValue().toString();
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.CURRENCY) {
					value = companyInvestmentItemResource.getValue().toString();
					if (companyInvestmentItemResource.getCurrency() != null) {
						companyInvestmentItem.setCurrency(companyInvestmentItemResource.getCurrency().getCode());
					}
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.DATE) {
					value = ISODateTimeFormat.dateTime().parseLocalDateTime(companyInvestmentItemResource.getValue().toString()).toLocalDate().toString();
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.RADIO) {
					value = companyInvestmentItemResource.getValue().toString();
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.SELECT) {
					ItemOptionResource itemOptionResource = objectMapper.readValue(objectMapper.writeValueAsString(companyInvestmentItemResource.getValue()), ItemOptionResource.class);
					value = itemOptionResource.getId().toString();
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.ACTIVITY) {
					ActivityResource resource = objectMapper.readValue(objectMapper.writeValueAsString(companyInvestmentItemResource.getValue()), ActivityResource.class);
					value = resource.getId().toString();
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.SUBDIVISION1) {
					Subdivision1Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(companyInvestmentItemResource.getValue()), Subdivision1Resource.class);
					value = resource.getId().toString();
				} else if (companyInvestmentItemResource.getItem().getType() == ItemType.SUBDIVISION2) {
					Subdivision2Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(companyInvestmentItemResource.getValue()), Subdivision2Resource.class);
					value = resource.getId().toString();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		companyInvestmentItem.setValue(value);
	}

}
