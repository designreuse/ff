package org.ff.rest.company.resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Activity;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.ActivityRepository;
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
import org.ff.rest.item.resource.ItemResource;
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
public class CompanyResourceAssembler {

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

	public CompanyResource toResource(Company entity, boolean light) {
		CompanyResource resource = new CompanyResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setCode(entity.getCode());

		if (!light) {
			// items
			resource.setItems(itemResourceAssembler.toResources(itemRepository.findByEntityTypeAndStatusOrderByPosition(ItemEntityType.COMPANY, ItemStatus.ACTIVE), false));

			for (ItemResource itemResource : resource.getItems()) {
				for (CompanyItem companyItem : entity.getItems()) {
					if (itemResource != null && itemResource.getId() != null && companyItem != null && companyItem.getItem() != null) {
						if (itemResource.getId().equals(companyItem.getItem().getId())) {
							setResourceValue(itemResource, companyItem);
							break;
						}
					}
				}

				if (itemResource.getType() == ItemType.CURRENCY && itemResource.getCurrency() == null) {
					itemResource.setCurrency(currencyService.findAll().get(0));
				}
			}
		}

		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<CompanyResource> toResources(Iterable<Company> entities, boolean light) {
		List<CompanyResource> resources = new ArrayList<>();
		for (Company entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Company createEntity(CompanyResource resource) {
		Company entity = new Company();
		entity.setName(resource.getName());
		entity.setCode(resource.getCode());
		return entity;
	}

	public Company updateEntity(Company entity, CompanyResource resource) {
		entity.setName(resource.getName());
		entity.setCode(resource.getCode());

		if (entity.getItems() == null) {
			entity.setItems(new LinkedHashSet<CompanyItem>());
		}

		if (resource.getItems() != null && !resource.getItems().isEmpty()) {
			for (ItemResource itemResource : resource.getItems()) {
				Item item = itemRepository.findOne(itemResource.getId());

				CompanyItem companyItem = null;

				// existing company item
				for (CompanyItem companyItemTmp : entity.getItems()) {
					if (companyItemTmp != null && companyItemTmp.getItem() != null && itemResource != null && itemResource.getId() != null) {
						if (companyItemTmp.getItem().getId().equals(itemResource.getId())) {
							companyItem = companyItemTmp;
							break;
						}
					}
				}

				// new company item
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setCompany(entity);
					companyItem.setItem(item);
				}

				// set company item value
				setEntityValue(itemResource, companyItem);

				entity.getItems().add(companyItem);
			}
		}

		return entity;
	}

	private void setResourceValue(ItemResource itemResource, CompanyItem companyItem) {
		try {
			if (itemResource.getType() == ItemType.NUMBER) {
				itemResource.setValue((companyItem.getValue() != null) ? Integer.parseInt(companyItem.getValue()) : null);
				itemResource.setValueMapped(companyItem.getValue());
			} else if (itemResource.getType() == ItemType.CURRENCY) {
				itemResource.setValue((companyItem.getValue() != null) ? Integer.parseInt(companyItem.getValue()) : null);
				itemResource.setValueMapped(companyItem.getValue());
				if (itemResource.getType() == ItemType.CURRENCY) {
					if (StringUtils.isNotBlank(companyItem.getCurrency())) {
						itemResource.setCurrency(new CurrencyResource(companyItem.getCurrency()));
					} else {
						itemResource.setCurrency(new CurrencyResource(currencyService.findAll().get(0).getCode()));
					}
				}
			} else if (itemResource.getType() == ItemType.DATE) {
				itemResource.setValue(companyItem.getValue());
				itemResource.setValueMapped(companyItem.getValue());
			} else if (itemResource.getType() == ItemType.RADIO) {
				if (companyItem.getValue() != null) {
					try {
						ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue()));
						if (itemOption != null) {
							itemResource.setValue(Integer.parseInt(companyItem.getValue()));
							itemResource.setValueMapped(itemOption.getText());
						}
					} catch (NumberFormatException e) {
						log.warn(e.getMessage(), e);
					}
				}
			} else if (itemResource.getType() == ItemType.SELECT) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue()));
					itemResource.setValue((itemOption != null) ? itemOptionResourceAssembler.toResource(itemOption, true) : null);
					itemResource.setValueMapped((itemOption != null) ? itemOption.getText() : null);
				}
			} else if (itemResource.getType() == ItemType.MULTISELECT) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					List<ItemOptionResource> value = new ArrayList<>();
					List<String> valueMapped = new ArrayList<>();
					for (String id : companyItem.getValue().split("\\|")) {
						ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(id));
						if (itemOption != null) {
							value.add(itemOptionResourceAssembler.toResource(itemOption, true));
							valueMapped.add(itemOption.getText());
						}
					}
					itemResource.setValue(value);
					itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
				}
			} else if (itemResource.getType() == ItemType.ACTIVITY) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					Activity entity = activityRepository.findOne(Integer.parseInt(companyItem.getValue()));
					if (entity != null) {
						itemResource.setValue(activityResourceAssembler.toResource(entity, true));
						itemResource.setValueMapped(entity.getName());
					}
				}
			} else if (itemResource.getType() == ItemType.SUBDIVISION1) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					Subdivision1 entity = subdivision1Repository.findOne(Integer.parseInt(companyItem.getValue()));
					if (entity != null) {
						itemResource.setValue(subdivision1ResourceAssembler.toResource(entity, true));
						itemResource.setValueMapped(entity.getName());
					}
				}
			} else if (itemResource.getType() == ItemType.SUBDIVISION2) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					Subdivision2 entity = subdivision2Repository.findOne(Integer.parseInt(companyItem.getValue()));
					if (entity != null) {
						itemResource.setValue(subdivision2ResourceAssembler.toResource(entity, true));
						itemResource.setValueMapped(entity.getName());
					}
				}
			} else {
				itemResource.setValue(companyItem.getValue());
				itemResource.setValueMapped(companyItem.getValue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public void setEntityValue(ItemResource itemResource, CompanyItem companyItem) {
		String value = null;

		try {
			if (itemResource.getValue() != null) {
				if (itemResource.getType() == ItemType.TEXT || itemResource.getType() == ItemType.TEXT_AREA || itemResource.getType() == ItemType.NUMBER) {
					value = itemResource.getValue().toString();
				} else if (itemResource.getType() == ItemType.CURRENCY) {
					value = itemResource.getValue().toString();
					if (itemResource.getCurrency() != null) {
						companyItem.setCurrency(itemResource.getCurrency().getCode());
					}
				} else if (itemResource.getType() == ItemType.DATE) {
					value = ISODateTimeFormat.dateTime().parseLocalDateTime(itemResource.getValue().toString()).toLocalDate().toString();
				} else if (itemResource.getType() == ItemType.RADIO) {
					value = itemResource.getValue().toString();
				} else if (itemResource.getType() == ItemType.SELECT) {
					ItemOptionResource itemOptionResource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), ItemOptionResource.class);
					value = itemOptionResource.getId().toString();
				} else if (itemResource.getType() == ItemType.MULTISELECT) {
					List<String> values = null;
					if (itemResource.getValue() != null) {
						values = new ArrayList<>();
						List<Object> objects = (List<Object>) itemResource.getValue();
						for (Object object : objects) {
							try {
								ItemOptionResource itemOptionResource = objectMapper.readValue(objectMapper.writeValueAsString(object), ItemOptionResource.class);
								values.add(itemOptionResource.getId().toString());
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}
						}
					}
					value = (values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null;
				} else if (itemResource.getType() == ItemType.ACTIVITY) {
					ActivityResource resource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), ActivityResource.class);
					value = resource.getId().toString();
				} else if (itemResource.getType() == ItemType.SUBDIVISION1) {
					Subdivision1Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), Subdivision1Resource.class);
					value = resource.getId().toString();
				} else if (itemResource.getType() == ItemType.SUBDIVISION2) {
					Subdivision2Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), Subdivision2Resource.class);
					value = resource.getId().toString();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		companyItem.setValue(value);
	}

}
