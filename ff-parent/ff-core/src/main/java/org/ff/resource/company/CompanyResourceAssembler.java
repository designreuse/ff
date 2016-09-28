package org.ff.resource.company;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.City;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Nkd;
import org.ff.jpa.repository.CityRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.NkdRepository;
import org.ff.resource.city.CityResource;
import org.ff.resource.city.CityResourceAssembler;
import org.ff.resource.investment.InvestmentResourceAssembler;
import org.ff.resource.item.ItemOptionResource;
import org.ff.resource.item.ItemOptionResourceAssembler;
import org.ff.resource.item.ItemResource;
import org.ff.resource.item.ItemResourceAssembler;
import org.ff.resource.nkd.NkdResource;
import org.ff.resource.nkd.NkdResourceAssembler;
import org.joda.time.DateTime;
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
	private InvestmentResourceAssembler investmentResourceAssembler;

	@Autowired
	private NkdRepository nkdRepository;

	@Autowired
	private NkdResourceAssembler nkdResourceAssembler;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CityResourceAssembler cityResourceAssembler;

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
					if (itemResource.getId().equals(companyItem.getItem().getId())) {
						setResourceValue(itemResource, companyItem);
						break;
					}
				}
			}

			// investments
			resource.setInvestments((entity.getInvestments() != null) ? investmentResourceAssembler.toResources(entity.getInvestments(), true) : null);
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
					if (companyItemTmp.getItem().getId().equals(itemResource.getId())) {
						companyItem = companyItemTmp;
						break;
					}
				}

				// new company item
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setCompany(entity);
					companyItem.setItem(item);
				}

				// set company item value
				setEntityValue(item, itemResource, companyItem);

				entity.getItems().add(companyItem);
			}
		}

		return entity;
	}

	private void setResourceValue(ItemResource itemResource, CompanyItem companyItem) {
		try {
			if (itemResource.getType() == ItemType.NUMBER) {
				itemResource.setValue(Integer.parseInt(companyItem.getValue()));
				itemResource.setValueMapped(companyItem.getValue());
			} else if (itemResource.getType() == ItemType.DATE) {
				itemResource.setValue(companyItem.getValue());
				itemResource.setValueMapped(companyItem.getValue());
			} else if (itemResource.getType() == ItemType.RADIO) {
				itemResource.setValue(Integer.parseInt(companyItem.getValue()));
				itemResource.setValueMapped(itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText());
			} else if (itemResource.getType() == ItemType.SELECT) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue()));
					itemResource.setValue(itemOptionResourceAssembler.toResource(itemOption, true));
					itemResource.setValueMapped(itemOption.getText());
				}
			} else if (itemResource.getType() == ItemType.CITY) {
				City city = cityRepository.findOne(Integer.parseInt(companyItem.getValue().toString()));
				itemResource.setValue(cityResourceAssembler.toResource(city, true));
				itemResource.setValueMapped(city.getName());
			} else if (itemResource.getType() == ItemType.NKD) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					Nkd nkd = nkdRepository.findOne(Integer.parseInt(companyItem.getValue()));
					itemResource.setValue(nkdResourceAssembler.toResource(nkd, true));
					itemResource.setValueMapped(nkd.getArea() + "." + nkd.getActivity() + " - " + nkd.getActivityName());
				}
			} else if (itemResource.getType() == ItemType.NKDS) {
				if (StringUtils.isNotBlank(companyItem.getValue())) {
					List<NkdResource> value = new ArrayList<>();
					List<String> valueMapped = new ArrayList<>();
					for (String id : companyItem.getValue().split("\\|")) {
						Nkd nkd = nkdRepository.findOne(Integer.parseInt(id));
						value.add(nkdResourceAssembler.toResource(nkd, true));
						valueMapped.add(nkd.getArea() + "." + nkd.getActivity() + " - " + nkd.getActivityName());
					}
					itemResource.setValue(value);
					itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
				}
			} else {
				itemResource.setValue(companyItem.getValue());
				itemResource.setValueMapped(companyItem.getValue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void setEntityValue(Item item, ItemResource itemResource, CompanyItem companyItem) {
		String value = null;

		try {
			if (itemResource.getValue() != null) {
				if (item.getType() == ItemType.NUMBER) {
					value = itemResource.getValue().toString();
				} if (item.getType() == ItemType.DATE) {
					value = new DateTime(itemResource.getValue()).toString();
				} else if (item.getType() == ItemType.RADIO) {
					value = itemResource.getValue().toString();
				} else if (item.getType() == ItemType.SELECT) {
					ItemOptionResource itemOptionResource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), ItemOptionResource.class);
					value = itemOptionResource.getId().toString();
				} else if (item.getType() == ItemType.CITY) {
					CityResource cityResource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), CityResource.class);
					value = cityResource.getId().toString();
				} else if (item.getType() == ItemType.NKD) {
					NkdResource nkdResource = objectMapper.readValue(objectMapper.writeValueAsString(itemResource.getValue()), NkdResource.class);
					value = nkdResource.getId().toString();
				} else if (item.getType() == ItemType.NKDS) {
					if (itemResource.getValue() != null) {
						List<?> objects = (List<?>) itemResource.getValue();
						if (!objects.isEmpty()) {
							List<String> values = new ArrayList<>();
							for (Object object : objects) {
								NkdResource nkdResource = objectMapper.readValue(objectMapper.writeValueAsString(object), NkdResource.class);
								values.add(nkdResource.getId().toString());
							}
							value = StringUtils.join(values, "|");
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		companyItem.setValue(value);
	}

}
