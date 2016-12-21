package org.ff.rest.subdivision1.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Subdivision1;
import org.springframework.stereotype.Component;

@Component
public class Subdivision1ResourceAssembler {

	public Subdivision1Resource toResource(Subdivision1 entity, boolean light) {
		Subdivision1Resource resource = new Subdivision1Resource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setDevelopmentIndex(entity.getDevelopmentIndex());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<Subdivision1Resource> toResources(Iterable<Subdivision1> entities, boolean light) {
		List<Subdivision1Resource> resources = new ArrayList<>();
		for (Subdivision1 entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Subdivision1 createEntity(Subdivision1Resource resource) {
		Subdivision1 entity = new Subdivision1();
		entity.setName(resource.getName());
		entity.setDevelopmentIndex(resource.getDevelopmentIndex());
		return entity;
	}

	public Subdivision1 updateEntity(Subdivision1 entity, Subdivision1Resource resource) {
		entity.setName(resource.getName());
		entity.setDevelopmentIndex(resource.getDevelopmentIndex());
		return entity;
	}

}
