package org.ff.resource.tender;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.County;
import org.ff.jpa.domain.Image;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Nkd;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.repository.CountyRepository;
import org.ff.jpa.repository.ImageRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.NkdRepository;
import org.ff.resource.county.CountyResource;
import org.ff.resource.county.CountyResourceAssembler;
import org.ff.resource.image.ImageResourceAssembler;
import org.ff.resource.investment.InvestmentResource;
import org.ff.resource.investment.InvestmentResourceAssembler;
import org.ff.resource.item.ItemOptionResource;
import org.ff.resource.item.ItemOptionResourceAssembler;
import org.ff.resource.item.ItemResource;
import org.ff.resource.item.ItemResourceAssembler;
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
	private CountyRepository countyRepository;

	@Autowired
	private CountyResourceAssembler countyResourceAssembler;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private InvestmentResourceAssembler investmentResourceAssembler;

	@Autowired
	private NkdRepository nkdRepository;

	public TenderResource toResource(Tender entity, boolean light) {
		TenderResource resource = new TenderResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setName(entity.getName());
		if (!light) {
			resource.setText(entity.getText());
		}
		if (!light && entity.getImage() != null) {
			resource.setImage(imageResourceAssembler.toResource(entity.getImage()));
		}

		if (!light) {
			resource.setItems(itemResourceAssembler.toResources(itemRepository.findByEntityTypeAndStatusOrderByPosition(ItemEntityType.TENDER, ItemStatus.ACTIVE), false));

			Set<TenderItem> tenderItems = entity.getItems();

			for (ItemResource itemResource : resource.getItems()) {
				for (TenderItem tenderItem : tenderItems) {
					if (itemResource.getId().equals(tenderItem.getItem().getId())) {
						if (itemResource.getType() == ItemType.NUMBER) {
							itemResource.setValue((tenderItem.getValue() != null) ? Integer.parseInt(tenderItem.getValue()) : null);
							itemResource.setValueMapped(tenderItem.getValue());
						} else if (itemResource.getType() == ItemType.RADIO) {
							itemResource.setValue((tenderItem.getValue() != null) ? Integer.parseInt(tenderItem.getValue()) : null);
							itemResource.setValueMapped((tenderItem.getValue() != null) ? itemOptionRepository.findOne(Integer.parseInt(tenderItem.getValue())).getText() : null);
						} else if (itemResource.getType() == ItemType.COUNTIES) {
							if (StringUtils.isNotBlank(tenderItem.getValue())) {
								List<CountyResource> value = new ArrayList<>();
								List<String> valueMapped = new ArrayList<>();
								for (String id : tenderItem.getValue().split("\\|")) {
									County county = countyRepository.findOne(Integer.parseInt(id));
									value.add(countyResourceAssembler.toResource(county, true));
									valueMapped.add(county.getName());
								}
								itemResource.setValue(value);
								itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
							}
						} else if (itemResource.getType() == ItemType.NKDS) {
							if (StringUtils.isNotBlank(tenderItem.getValue())) {
								List<Integer> value = new ArrayList<>();
								List<String> valueMapped = new ArrayList<>();
								for (String id : tenderItem.getValue().split("\\|")) {
									value.add(Integer.parseInt(id));
									Nkd nkd = nkdRepository.findOne(Integer.parseInt(id));
									valueMapped.add(nkd.getArea() + "." + nkd.getActivity() + " - " + nkd.getActivityName());
								}
								itemResource.setValue(value);
								itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
							}
						} else if (itemResource.getType() == ItemType.INVESTMENTS) {
							if (StringUtils.isNotBlank(tenderItem.getValue())) {
								List<InvestmentResource> value = new ArrayList<>();
								List<String> valueMapped = new ArrayList<>();
								for (String id : tenderItem.getValue().split("\\|")) {
									Investment investment = investmentRepository.findOne(Integer.parseInt(id));
									value.add(investmentResourceAssembler.toResource(investment, true));
									valueMapped.add(investment.getName());
								}
								itemResource.setValue(value);
								itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
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
								itemResource.setValue(itemOptionResourceAssembler.toResource(itemOption, true));
								itemResource.setValueMapped(itemOption.getText());
							}
						} else if (itemResource.getType() == ItemType.MULTISELECT) {
							if (StringUtils.isNotBlank(tenderItem.getValue())) {
								List<ItemOptionResource> value = new ArrayList<>();
								List<String> valueMapped = new ArrayList<>();
								for (String id : tenderItem.getValue().split("\\|")) {
									ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(id));
									value.add(itemOptionResourceAssembler.toResource(itemOption, true));
									valueMapped.add(itemOption.getText());
								}
								itemResource.setValue(value);
								itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
							}
						} else {
							itemResource.setValue(tenderItem.getValue());
							itemResource.setValueMapped(tenderItem.getValue());
						}
						break;
					}
				}
			}
		}

		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<TenderResource> toResources(List<Tender> entities, boolean light) {
		List<TenderResource> resources = new ArrayList<>();
		for (Tender entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	@SuppressWarnings("unchecked")
	public Tender createEntity(TenderResource resource) {
		Tender entity = new Tender();
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : Tender.TenderStatus.INACTIVE);
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		Image image = new Image();
		if (resource.getImage() != null) {
			image.setBase64(resource.getImage().getBase64());
		}
		entity.setImage(imageRepository.save(image));

		// tender items
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

				if (item.getType() == ItemType.COUNTIES) {
					List<String> values = null;
					if (itemResource.getValue() != null) {
						values = new ArrayList<>();
						List<Object> objects = (List<Object>) itemResource.getValue();
						for (Object object : objects) {
							try {
								CountyResource countyResource = objectMapper.readValue(objectMapper.writeValueAsString(object), CountyResource.class);
								values.add(countyResource.getId().toString());
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}
						}
					}
					tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
				} else if (item.getType() == ItemType.NKDS) {
					List<String> values = null;
					if (itemResource.getValue() != null) {
						values = new ArrayList<>();
						List<Object> objects = (List<Object>) itemResource.getValue();
						for (Object object : objects) {
							values.add(object.toString());
						}
					}
					tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
				} else if (item.getType() == ItemType.INVESTMENTS) {
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
				} else {
					tenderItem.setValue((itemResource.getValue() != null) ? itemResource.getValue().toString() : null);
				}

				entity.getItems().add(tenderItem);
			}
		}

		return entity;
	}

	@SuppressWarnings("unchecked")
	public Tender updateEntity(Tender entity, TenderResource resource) {
		entity.setStatus(resource.getStatus());
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		if (resource.getImage() != null) {
			Image image = imageRepository.findOne(resource.getImage().getId());
			image.setBase64(resource.getImage().getBase64());
			imageRepository.save(image);
			entity.setImage(image);
		}

		// tender items
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

				if (item.getType() == ItemType.COUNTIES) {
					List<String> values = null;
					if (itemResource.getValue() != null) {
						values = new ArrayList<>();
						List<Object> objects = (List<Object>) itemResource.getValue();
						for (Object object : objects) {
							try {
								CountyResource countyResource = objectMapper.readValue(objectMapper.writeValueAsString(object), CountyResource.class);
								values.add(countyResource.getId().toString());
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}
						}
					}
					tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
				} else if (item.getType() == ItemType.NKDS) {
					List<String> values = null;
					if (itemResource.getValue() != null) {
						values = new ArrayList<>();
						List<Integer> ids = (List<Integer>) itemResource.getValue();
						for (Integer id : ids) {
							values.add(id.toString());
						}
					}
					tenderItem.setValue((values != null && !values.isEmpty()) ? StringUtils.join(values, "|") : null);
				} else if (item.getType() == ItemType.INVESTMENTS) {
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
				} else {
					tenderItem.setValue((itemResource.getValue() != null) ? itemResource.getValue().toString() : null);
				}

				entity.getItems().add(tenderItem);
			}
		}

		return entity;
	}

}
