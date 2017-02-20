package org.ff.rest.tender.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Activity;
import org.ff.jpa.domain.Image;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.repository.ActivityRepository;
import org.ff.jpa.repository.ImageRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.rest.activity.resource.ActivityResource;
import org.ff.rest.activity.resource.ActivityResourceAssembler;
import org.ff.rest.currency.resource.CurrencyResource;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.image.resource.ImageResourceAssembler;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.investment.resource.InvestmentResourceAssembler;
import org.ff.rest.item.resource.ItemOptionResource;
import org.ff.rest.item.resource.ItemOptionResourceAssembler;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.ff.rest.subdivision1.resource.Subdivision1Resource;
import org.ff.rest.subdivision1.resource.Subdivision1ResourceAssembler;
import org.ff.rest.subdivision2.resource.Subdivision2Resource;
import org.ff.rest.subdivision2.resource.Subdivision2ResourceAssembler;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TenderResourceAssembler {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ImageResourceAssembler imageResourceAssembler;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	@Autowired
	private ItemOptionResourceAssembler itemOptionResourceAssembler;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private InvestmentResourceAssembler investmentResourceAssembler;

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
	private ProjectResourceAssembler projectResourceAssembler;

	public TenderResource toResource(Tender entity, boolean light) {
		TenderResource resource = new TenderResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setName(entity.getName());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());

		List<Item> items = itemRepository.findByEntityTypeAndStatusOrderByPosition(ItemEntityType.TENDER, ItemStatus.ACTIVE);
		Set<TenderItem> tenderItems = entity.getItems();

		resource.setIncomplete(Boolean.FALSE);
		for (Item item : items) {
			if (Boolean.TRUE == item.getMandatory()) {
				Boolean found = Boolean.FALSE;
				for (TenderItem tenderItem : tenderItems) {
					if (item.getId().equals(tenderItem.getItem().getId())) {
						found = Boolean.TRUE;
						if (StringUtils.isBlank(tenderItem.getValue())) {
							resource.setIncomplete(Boolean.TRUE);
							break;
						}
					}
				}

				if (Boolean.FALSE == found) {
					resource.setIncomplete(Boolean.TRUE);
					break;
				}

				if (Boolean.TRUE == resource.getIncomplete()) {
					break;
				}
			}
		}

		resource.setProjects(projectResourceAssembler.toResources(entity.getProjects(), true));

		// go through all items to find out if some value is invalid (e.g. item option with that ID no longer exists)
		resource.setItems(itemResourceAssembler.toResources(items, false));
		for (ItemResource itemResource : resource.getItems()) {
			for (TenderItem tenderItem : tenderItems) {
				if (itemResource.getId().equals(tenderItem.getItem().getId())) {
					setResourceValue(resource, itemResource, tenderItem);
					break;
				}
			}

			if (itemResource.getType() == ItemType.CURRENCY && itemResource.getCurrency() == null) {
				itemResource.setCurrency(currencyService.findAll().get(0));
			}
		}

		if (!light) {
			resource.setCreatedBy(entity.getCreatedBy());
			resource.setLastModifiedBy(entity.getLastModifiedBy());
			resource.setText(entity.getText());
			resource.setImage((entity.getImage() != null) ? imageResourceAssembler.toResource(entity.getImage(), false) : null);
		} else {
			// nullify items is light resource is requested
			resource.setItems(null);
		}

		return resource;
	}

	public List<TenderResource> toResources(Iterable<Tender> entities, boolean light) {
		List<TenderResource> resources = new ArrayList<>();
		for (Tender entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Tender createEntity(TenderResource resource) {
		Tender entity = new Tender();
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : Tender.TenderStatus.INACTIVE);
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		Image image = new Image();
		image.setBase64((resource.getImage() != null) ? resource.getImage().getBase64() : null);
		entity.setImage(imageRepository.save(image));

		if (entity.getItems() == null) {
			entity.setItems(new LinkedHashSet<TenderItem>());
		}

		if (resource.getItems() != null && !resource.getItems().isEmpty()) {
			for (ItemResource itemResource : resource.getItems()) {
				TenderItem tenderItem = null;
				for (TenderItem tenderItemTmp : entity.getItems()) {
					if (tenderItemTmp.getItem().getId().equals(itemResource.getId())) {
						tenderItem = tenderItemTmp;
						break;
					}
				}

				Item item = itemRepository.findOne(itemResource.getId());
				if (tenderItem == null) {
					tenderItem = new TenderItem();
					tenderItem.setTender(entity);
					tenderItem.setItem(item);
				}

				setEntityValue(item, itemResource, tenderItem, false);
				entity.getItems().add(tenderItem);
			}
		}

		return entity;
	}

	public Tender updateEntity(Tender entity, TenderResource resource) {
		entity.setStatus(resource.getStatus());
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		Image image = imageRepository.findOne(resource.getImage().getId());
		image.setBase64((resource.getImage() != null) ? resource.getImage().getBase64() : null);
		entity.setImage(imageRepository.save(image));

		if (entity.getItems() == null) {
			entity.setItems(new LinkedHashSet<TenderItem>());
		}

		if (resource.getItems() != null && !resource.getItems().isEmpty()) {
			for (ItemResource itemResource : resource.getItems()) {
				TenderItem tenderItem = null;
				for (TenderItem tenderItemTmp : entity.getItems()) {
					if (tenderItemTmp.getItem().getId().equals(itemResource.getId())) {
						tenderItem = tenderItemTmp;
						break;
					}
				}

				Item item = itemRepository.findOne(itemResource.getId());
				if (tenderItem == null) {
					tenderItem = new TenderItem();
					tenderItem.setTender(entity);
					tenderItem.setItem(item);
				}

				setEntityValue(item, itemResource, tenderItem, false);
				entity.getItems().add(tenderItem);
			}
		}

		return entity;
	}

	public void setResourceValue(TenderResource resource, ItemResource itemResource, TenderItem tenderItem) {
		if (itemResource.getType() == ItemType.NUMBER || itemResource.getType() == ItemType.CURRENCY || itemResource.getType() == ItemType.PERCENTAGE) {
			itemResource.setValue((tenderItem.getValue() != null) ? Double.parseDouble(tenderItem.getValue()) : null);
			itemResource.setValueMapped(tenderItem.getValue());
			if (itemResource.getType() == ItemType.CURRENCY) {
				if (StringUtils.isNotBlank(tenderItem.getCurrency())) {
					itemResource.setCurrency(new CurrencyResource(tenderItem.getCurrency()));
				} else {
					itemResource.setCurrency(new CurrencyResource(currencyService.findAll().get(0).getCode()));
				}
			}
		} else if (itemResource.getType() == ItemType.DATE) {
			itemResource.setValue(tenderItem.getValue());
			itemResource.setValueMapped(tenderItem.getValue());
		} else if (itemResource.getType() == ItemType.RADIO) {
			if (tenderItem.getValue() != null) {
				try {
					ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(tenderItem.getValue()));
					if (itemOption != null) {
						itemResource.setValue(Integer.parseInt(tenderItem.getValue()));
						itemResource.setValueMapped(itemOption.getText());
					}
				} catch (NumberFormatException e) {
					log.warn(e.getMessage(), e);
				}
			}
		} else if (itemResource.getType() == ItemType.CHECKBOX) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				Map<String, Boolean> map = new LinkedHashMap<>();
				List<String> valueMapped = new ArrayList<>();
				for (ItemOptionResource itemOptionResource : itemResource.getOptions()) {
					map.put(itemOptionResource.getId().toString(), Boolean.FALSE);
					for (String id : tenderItem.getValue().split("\\|")) {
						if (itemOptionResource.getId().toString().equals(id)) {
							map.put(itemOptionResource.getId().toString(), Boolean.TRUE);
							valueMapped.add(itemOptionResource.getText());
							break;
						}
					}
				}
				itemResource.setValue(map);
				itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
			}
		} else if (itemResource.getType() == ItemType.SELECT) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(tenderItem.getValue()));
				if (itemOption != null) {
					itemResource.setValue(itemOptionResourceAssembler.toResource(itemOption, true));
					itemResource.setValueMapped(itemOption.getText());
				} else {
					resource.setIncomplete(Boolean.TRUE);
				}
			}
		} else if (itemResource.getType() == ItemType.MULTISELECT) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				List<ItemOptionResource> value = new ArrayList<>();
				List<String> valueMapped = new ArrayList<>();
				for (String id : tenderItem.getValue().split("\\|")) {
					ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(id));
					if (itemOption != null) {
						value.add(itemOptionResourceAssembler.toResource(itemOption, true));
						valueMapped.add(itemOption.getText());
					} else {
						resource.setIncomplete(Boolean.TRUE);
					}
				}
				itemResource.setValue(value);
				itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
			}
		} else if (itemResource.getType() == ItemType.ACTIVITIES) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				List<ActivityResource> value = new ArrayList<>();
				List<String> valueMapped = new ArrayList<>();
				for (String id : tenderItem.getValue().split("\\|")) {
					Activity entity = activityRepository.findOne(Integer.parseInt(id));
					if (entity != null) {
						value.add(activityResourceAssembler.toResource(entity, true));
						valueMapped.add(entity.getName());
					} else {
						resource.setIncomplete(Boolean.TRUE);
					}
				}
				itemResource.setValue(value);
				itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
			}
		} else if (itemResource.getType() == ItemType.SUBDIVISIONS1) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				List<Subdivision1Resource> value = new ArrayList<>();
				List<String> valueMapped = new ArrayList<>();
				for (String id : tenderItem.getValue().split("\\|")) {
					Subdivision1 entity = subdivision1Repository.findOne(Integer.parseInt(id));
					if (entity != null) {
						value.add(subdivision1ResourceAssembler.toResource(entity, true));
						valueMapped.add(entity.getName());
					} else {
						resource.setIncomplete(Boolean.TRUE);
					}
				}
				itemResource.setValue(value);
				itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
			}
		} else if (itemResource.getType() == ItemType.SUBDIVISIONS2) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				List<Subdivision2Resource> value = new ArrayList<>();
				List<String> valueMapped = new ArrayList<>();
				for (String id : tenderItem.getValue().split("\\|")) {
					Subdivision2 entity = subdivision2Repository.findOne(Integer.parseInt(id));
					if (entity != null) {
						value.add(subdivision2ResourceAssembler.toResource(entity, true));
						valueMapped.add(entity.getName());
					} else {
						resource.setIncomplete(Boolean.TRUE);
					}
				}
				itemResource.setValue(value);
				itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
			}
		} else if (itemResource.getType() == ItemType.INVESTMENTS_PRIMARY || itemResource.getType() == ItemType.INVESTMENTS_SECONDARY) {
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				List<InvestmentResource> value = new ArrayList<>();
				List<String> valueMapped = new ArrayList<>();
				for (String id : tenderItem.getValue().split("\\|")) {
					Investment entity = investmentRepository.findOne(Integer.parseInt(id));
					if (entity != null) {
						value.add(investmentResourceAssembler.toResource(entity, true));
						valueMapped.add(entity.getName());
					} else {
						resource.setIncomplete(Boolean.TRUE);
					}
				}
				itemResource.setValue(value);
				itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
			}
		} else {
			itemResource.setValue(tenderItem.getValue());
			itemResource.setValueMapped(tenderItem.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	public void setEntityValue(Item item, ItemResource itemResource, TenderItem tenderItem, boolean isImport) {
		if (itemResource.getType() == ItemType.DATE) {
			if (isImport) {
				String value = (itemResource.getValue() != null) ? DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(itemResource.getValue().toString()).toLocalDate().toString() : null;
				tenderItem.setValue(value);
			} else {
				String value = (itemResource.getValue() != null) ? ISODateTimeFormat.dateTime().parseLocalDateTime(itemResource.getValue().toString()).toLocalDate().toString() : null;
				tenderItem.setValue(value);
			}
		} else if (item.getType() == ItemType.CURRENCY) {
			tenderItem.setValue((itemResource.getValue() != null) ? itemResource.getValue().toString() : null);
			if (itemResource.getCurrency() != null) {
				tenderItem.setCurrency(itemResource.getCurrency().getCode());
			}
		} else if (item.getType() == ItemType.CHECKBOX) {
			List<String> values = null;
			if (itemResource.getValue() != null) {
				values = new ArrayList<>();

				if (itemResource.getValue() instanceof String) {
					values.add(itemResource.getValue().toString());
				} else {
					Map<String, Boolean> map = (LinkedHashMap<String, Boolean>) itemResource.getValue();
					for (Entry<String, Boolean> entry : map.entrySet()) {
						if (entry.getValue() == Boolean.TRUE) {
							values.add(entry.getKey());
						}
					}
				}
			}

			tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
		} else if (item.getType() == ItemType.SELECT) {
			String value = null;
			if (itemResource.getValue() != null) {
				try {
					ItemOptionResource itemOptionResource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), ItemOptionResource.class);
					value = itemOptionResource.getId().toString();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

			tenderItem.setValue(value);
		} else if (item.getType() == ItemType.MULTISELECT) {
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
			tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
		} else if (item.getType() == ItemType.ACTIVITIES) {
			List<String> values = null;
			if (itemResource.getValue() != null) {
				values = new ArrayList<>();
				List<Object> objects = (List<Object>) itemResource.getValue();
				for (Object object : objects) {
					try {
						ActivityResource resource = objectMapper.readValue(objectMapper.writeValueAsString(object), ActivityResource.class);
						values.add(resource.getId().toString());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
		} else if (item.getType() == ItemType.SUBDIVISIONS1) {
			List<String> values = null;
			if (itemResource.getValue() != null) {
				values = new ArrayList<>();
				List<Object> objects = (List<Object>) itemResource.getValue();
				for (Object object : objects) {
					try {
						Subdivision1Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(object), Subdivision1Resource.class);
						values.add(resource.getId().toString());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
		} else if (item.getType() == ItemType.SUBDIVISIONS2) {
			List<String> values = null;
			if (itemResource.getValue() != null) {
				values = new ArrayList<>();
				List<Object> objects = (List<Object>) itemResource.getValue();
				for (Object object : objects) {
					try {
						Subdivision2Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(object), Subdivision2Resource.class);
						values.add(resource.getId().toString());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
		} else if (item.getType() == ItemType.INVESTMENTS_PRIMARY || item.getType() == ItemType.INVESTMENTS_SECONDARY) {
			List<String> values = null;
			if (itemResource.getValue() != null) {
				values = new ArrayList<>();
				List<Object> objects = (List<Object>) itemResource.getValue();
				for (Object object : objects) {
					try {
						InvestmentResource investmentResource = objectMapper.readValue(objectMapper.writeValueAsString(object), InvestmentResource.class);
						values.add(investmentResource.getId().toString());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
		} else {
			tenderItem.setValue((itemResource.getValue() != null) ? itemResource.getValue().toString() : null);
		}
	}

}
