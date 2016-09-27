package org.ff.resource.company;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Nkd;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.NkdRepository;
import org.ff.resource.investment.InvestmentResourceAssembler;
import org.ff.resource.item.ItemOptionResource;
import org.ff.resource.item.ItemOptionResourceAssembler;
import org.ff.resource.item.ItemResource;
import org.ff.resource.item.ItemResourceAssembler;
import org.ff.resource.nkd.NkdResource;
import org.ff.resource.nkd.NkdResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyResourceAssembler {

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

	public CompanyResource toResource(Company entity, boolean light) {
		CompanyResource resource = new CompanyResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setCode(entity.getCode());

		if (!light) {
			// items
			resource.setItems(itemResourceAssembler.toResources(itemRepository.findByEntityTypeAndStatusOrderByPosition(ItemEntityType.COMPANY, ItemStatus.ACTIVE), false));

			Set<CompanyItem> companyItems = entity.getItems();

			for (ItemResource itemResource : resource.getItems()) {
				for (CompanyItem companyItem : companyItems) {
					if (itemResource.getId().equals(companyItem.getItem().getId())) {
						if (itemResource.getType() == ItemType.NUMBER) {
							itemResource.setValue(Integer.parseInt(companyItem.getValue()));
							itemResource.setValueMapped(companyItem.getValue());
						} else if (itemResource.getType() == ItemType.RADIO) {
							itemResource.setValue(Integer.parseInt(companyItem.getValue()));
							itemResource.setValueMapped(itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText());
						} else if (itemResource.getType() == ItemType.CHECKBOX) {
							if (StringUtils.isNotBlank(companyItem.getValue())) {
								Map<String, Boolean> map = new LinkedHashMap<>();
								List<String> valueMapped = new ArrayList<>();
								for (ItemOptionResource itemOptionResource : itemResource.getOptions()) {
									map.put(itemOptionResource.getId().toString(), Boolean.FALSE);
									for (String id : companyItem.getValue().split("\\|")) {
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
							if (StringUtils.isNotBlank(companyItem.getValue())) {
								ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue()));
								itemResource.setValue(itemOptionResourceAssembler.toResource(itemOption, true));
								itemResource.setValueMapped(itemOption.getText());
							}
						} else if (itemResource.getType() == ItemType.MULTISELECT) {
							if (StringUtils.isNotBlank(companyItem.getValue())) {
								List<ItemOptionResource> value = new ArrayList<>();
								List<String> valueMapped = new ArrayList<>();
								for (String id : companyItem.getValue().split("\\|")) {
									ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(id));
									value.add(itemOptionResourceAssembler.toResource(itemOption, true));
									valueMapped.add(itemOption.getText());
								}
								itemResource.setValue(value);
								itemResource.setValueMapped(StringUtils.join(valueMapped, "<br>"));
							}
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
		return entity;
	}

}
