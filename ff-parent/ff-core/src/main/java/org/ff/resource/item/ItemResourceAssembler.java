package org.ff.resource.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.ItemOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemResourceAssembler {

	@Autowired
	private ItemOptionResourceAssembler itemOptionResourceAssembler;

	public ItemResource toResource(Item entity, boolean light) {
		ItemResource resource = new ItemResource();
		resource.setId(entity.getId());
		resource.setCode(entity.getCode());
		resource.setEntityType(entity.getEntityType());
		resource.setEntityId(entity.getEntityId());
		resource.setStatus(entity.getStatus());
		resource.setType(entity.getType());
		resource.setMandatory(entity.getMandatory());
		resource.setPosition(entity.getPosition());
		resource.setText(entity.getText());
		if (entity.getOptions() != null && !entity.getOptions().isEmpty()) {
			resource.setOptions(itemOptionResourceAssembler.toResources(entity.getOptions(), light));
		} else {
			resource.setOptions(new ArrayList<ItemOptionResource>());
		}
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<ItemResource> toResources(List<Item> entities, boolean light) {
		List<ItemResource> resources = new ArrayList<>();
		for (Item entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Item createEntity(ItemResource resource) {
		Item entity = new Item();
		entity.setCode(resource.getCode());
		entity.setEntityType(resource.getEntityType());
		entity.setEntityId(resource.getEntityId());
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : ItemStatus.INACTIVE);
		entity.setType(resource.getType());
		entity.setMandatory((resource.getMandatory() != null) ? resource.getMandatory() : Boolean.FALSE);
		entity.setPosition(resource.getPosition());
		entity.setText(resource.getText());

		if (entity.getOptions() == null) {
			entity.setOptions(new HashSet<ItemOption>());
		}
		entity.getOptions().clear();
		if (resource.getOptions() != null && !resource.getOptions().isEmpty()) {
			Set<ItemOption> options = itemOptionResourceAssembler.toEntities(resource.getOptions());
			for (ItemOption option : options) {
				option.setItem(entity);
				entity.getOptions().add(option);
			}
		}

		return entity;
	}

	public Item updateEntity(Item entity, ItemResource resource) {
		entity.setCode(resource.getCode());
		entity.setEntityType(resource.getEntityType());
		entity.setEntityId(resource.getEntityId());
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : ItemStatus.INACTIVE);
		entity.setType(resource.getType());
		entity.setMandatory((resource.getMandatory() != null) ? resource.getMandatory() : Boolean.FALSE);
		entity.setPosition(resource.getPosition());
		entity.setText(resource.getText());

		if (entity.getOptions() == null) {
			entity.setOptions(new LinkedHashSet<ItemOption>());
		}
		entity.getOptions().clear();
		if (resource.getOptions() != null && !resource.getOptions().isEmpty()) {
			Set<ItemOption> options = itemOptionResourceAssembler.toEntities(resource.getOptions());
			for (ItemOption option : options) {
				option.setItem(entity);
				entity.getOptions().add(option);
			}
		}

		return entity;
	}

}
