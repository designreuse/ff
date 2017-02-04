package org.ff.rest.project.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Activity;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Project;
import org.ff.jpa.domain.ProjectItem;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.ActivityRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.ProjectItemRepository;
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
public class ProjectItemResourceAssembler {

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
	private ProjectItemRepository projectItemRepository;

	public ProjectItemResource toResource(ProjectItem entity, boolean light) {
		ProjectItemResource resource = new ProjectItemResource();
		resource.setId(entity.getId());
		resource.setItem((entity.getItem() != null) ? itemResourceAssembler.toResource(entity.getItem(), light) : null);
		resource.setValue(entity.getValue());
		resource.setCurrency(new CurrencyResource(entity.getCurrency()));
		setResourceValue(resource, entity);
		return resource;
	}

	public List<ProjectItemResource> toResources(Iterable<ProjectItem> entities, boolean light) {
		List<ProjectItemResource> resources = new ArrayList<>();
		for (ProjectItem entity : entities) {
			resources.add(toResource(entity, light));
		}

		Collections.sort(resources, new Comparator<ProjectItemResource>() {
			@Override
			public int compare(ProjectItemResource o1, ProjectItemResource o2) {
				return o1.getItem().getPosition().compareTo(o2.getItem().getPosition());
			}
		});

		return resources;
	}

	public ProjectItem toEntity(ProjectItemResource resource, Project project) {
		ProjectItem entity = (resource.getId() == null) ? new ProjectItem() : projectItemRepository.findOne(resource.getId());
		entity.setProject(project);
		entity.setItem(itemRepository.findOne(resource.getItem().getId()));
		entity.setCurrency((resource.getCurrency() != null) ? resource.getCurrency().getCode() : null);
		setEntityValue(resource, entity);

		return entity;
	}

	public Set<ProjectItem> toEntities(List<ProjectItemResource> resources, Project entity) {
		Set<ProjectItem> entities = (entity.getItems() != null) ? entity.getItems() : new HashSet<ProjectItem>();
		for (ProjectItemResource resource : resources) {
			entities.add(toEntity(resource, entity));
		}
		return entities;
	}

	private void setResourceValue(ProjectItemResource itemResource, ProjectItem projectItem) {
		try {
			if (itemResource.getItem().getType() == ItemType.NUMBER) {
				itemResource.setValue((projectItem.getValue() != null) ? Integer.parseInt(projectItem.getValue()) : null);
				itemResource.setValueMapped((projectItem.getValue() != null) ? projectItem.getValue() : null);
			} else if (itemResource.getItem().getType() == ItemType.CURRENCY) {
				itemResource.setValue((projectItem.getValue() != null) ? Double.parseDouble(projectItem.getValue()) : null);
				if (itemResource.getItem().getType() == ItemType.CURRENCY) {
					if (StringUtils.isNotBlank(projectItem.getCurrency())) {
						itemResource.setCurrency(new CurrencyResource(projectItem.getCurrency()));
					} else {
						itemResource.setCurrency(new CurrencyResource(currencyService.findAll().get(0).getCode()));
					}
				}
				itemResource.setValueMapped(currencyService.format(projectItem.getValue(), itemResource.getCurrency().getCode()));
			} else if (itemResource.getItem().getType() == ItemType.DATE) {
				itemResource.setValue(projectItem.getValue());
			} else if (itemResource.getItem().getType() == ItemType.RADIO) {
				itemResource.setValue(Integer.parseInt(projectItem.getValue()));
			} else if (itemResource.getItem().getType() == ItemType.SELECT) {
				if (StringUtils.isNotBlank(projectItem.getValue())) {
					ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(projectItem.getValue()));
					itemResource.setValue(itemOptionResourceAssembler.toResource(itemOption, true));
					itemResource.setValueMapped(itemOption.getText());
				}
			} else if (itemResource.getItem().getType() == ItemType.ACTIVITY) {
				if (StringUtils.isNotBlank(projectItem.getValue())) {
					Activity entity = activityRepository.findOne(Integer.parseInt(projectItem.getValue()));
					itemResource.setValue(activityResourceAssembler.toResource(entity, true));
					itemResource.setValueMapped(entity.getName());
				}
			} else if (itemResource.getItem().getType() == ItemType.SUBDIVISION1) {
				if (StringUtils.isNotBlank(projectItem.getValue())) {
					Subdivision1 entity = subdivision1Repository.findOne(Integer.parseInt(projectItem.getValue()));
					itemResource.setValue(subdivision1ResourceAssembler.toResource(entity, true));
					itemResource.setValueMapped(entity.getName());
				}
			} else if (itemResource.getItem().getType() == ItemType.SUBDIVISION2) {
				if (StringUtils.isNotBlank(projectItem.getValue())) {
					Subdivision2 entity = subdivision2Repository.findOne(Integer.parseInt(projectItem.getValue()));
					itemResource.setValue(subdivision2ResourceAssembler.toResource(entity, true));
					itemResource.setValueMapped(entity.getName());
				}
			} else {
				itemResource.setValue(projectItem.getValue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void setEntityValue(ProjectItemResource projectItemResource, ProjectItem projectItem) {
		String value = null;

		try {
			if (projectItemResource.getValue() != null) {
				if (projectItemResource.getItem().getType() == ItemType.TEXT || projectItemResource.getItem().getType() == ItemType.TEXT_AREA || projectItemResource.getItem().getType() == ItemType.NUMBER) {
					value = projectItemResource.getValue().toString();
				} else if (projectItemResource.getItem().getType() == ItemType.CURRENCY) {
					value = projectItemResource.getValue().toString();
					if (projectItemResource.getCurrency() != null) {
						projectItem.setCurrency(projectItemResource.getCurrency().getCode());
					}
				} else if (projectItemResource.getItem().getType() == ItemType.DATE) {
					value = ISODateTimeFormat.dateTime().parseLocalDateTime(projectItemResource.getValue().toString()).toLocalDate().toString();
				} else if (projectItemResource.getItem().getType() == ItemType.RADIO) {
					value = projectItemResource.getValue().toString();
				} else if (projectItemResource.getItem().getType() == ItemType.SELECT) {
					ItemOptionResource itemOptionResource = objectMapper.readValue(objectMapper.writeValueAsString(projectItemResource.getValue()), ItemOptionResource.class);
					value = itemOptionResource.getId().toString();
				} else if (projectItemResource.getItem().getType() == ItemType.ACTIVITY) {
					ActivityResource resource = objectMapper.readValue(objectMapper.writeValueAsString(projectItemResource.getValue()), ActivityResource.class);
					value = resource.getId().toString();
				} else if (projectItemResource.getItem().getType() == ItemType.SUBDIVISION1) {
					Subdivision1Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(projectItemResource.getValue()), Subdivision1Resource.class);
					value = resource.getId().toString();
				} else if (projectItemResource.getItem().getType() == ItemType.SUBDIVISION2) {
					Subdivision2Resource resource = objectMapper.readValue(objectMapper.writeValueAsString(projectItemResource.getValue()), Subdivision2Resource.class);
					value = resource.getId().toString();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		projectItem.setValue(value);
	}

}
