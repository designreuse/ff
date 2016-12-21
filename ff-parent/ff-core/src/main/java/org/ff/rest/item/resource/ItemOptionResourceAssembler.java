package org.ff.rest.item.resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.repository.ItemOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemOptionResourceAssembler {

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	public ItemOptionResource toResource(ItemOption entity, boolean light) {
		ItemOptionResource resource = new ItemOptionResource();
		resource.setId(entity.getId());
		resource.setText(entity.getText());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<ItemOptionResource> toResources(Iterable<ItemOption> entities, boolean light) {
		List<ItemOptionResource> resources = new ArrayList<>();
		for (ItemOption entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public ItemOption toEntity(ItemOptionResource resource) {
		ItemOption entity = (resource.getId() == null) ? new ItemOption() : itemOptionRepository.findOne(resource.getId());
		entity.setText(resource.getText());
		return entity;
	}

	public Set<ItemOption> toEntities(Iterable<ItemOptionResource> resources) {
		Set<ItemOption> entities = new LinkedHashSet<>();
		int position = 0;
		for (ItemOptionResource resource : resources) {
			ItemOption entity = toEntity(resource);
			entity.setPosition(position++);
			entities.add(entity);
		}
		return entities;
	}

}
