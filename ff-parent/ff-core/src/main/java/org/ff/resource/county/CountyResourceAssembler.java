package org.ff.resource.county;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.County;
import org.springframework.stereotype.Component;

@Component
public class CountyResourceAssembler {

	public CountyResource toResource(County entity, boolean light) {
		CountyResource resource = new CountyResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<CountyResource> toResources(Iterable<County> entities, boolean light) {
		List<CountyResource> resources = new ArrayList<>();
		for (County entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public County createEntity(CountyResource resource) {
		County entity = new County();
		entity.setName(resource.getName());
		return entity;
	}

	public County updateEntity(County entity, CountyResource resource) {
		entity.setName(resource.getName());
		return entity;
	}

}
