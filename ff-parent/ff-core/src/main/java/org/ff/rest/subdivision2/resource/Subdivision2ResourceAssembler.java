package org.ff.rest.subdivision2.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.rest.subdivision1.resource.Subdivision1ResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Subdivision2ResourceAssembler {

	@Autowired
	private Subdivision1ResourceAssembler subdivision1ResourceAssembler;

	@Autowired
	private Subdivision1Repository subdivision1Repository;

	public Subdivision2Resource toResource(Subdivision2 entity, boolean light) {
		Subdivision2Resource resource = new Subdivision2Resource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setSubdivision1(subdivision1ResourceAssembler.toResource(entity.getSubdivision1(), light));
		resource.setDevelopmentIndex(entity.getDevelopmentIndex());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<Subdivision2Resource> toResources(Iterable<Subdivision2> entities, boolean light) {
		List<Subdivision2Resource> resources = new ArrayList<>();
		for (Subdivision2 entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Subdivision2 createEntity(Subdivision2Resource resource) {
		Subdivision2 entity = new Subdivision2();
		entity.setName(resource.getName());
		entity.setSubdivision1(subdivision1Repository.findOne(resource.getSubdivision1().getId()));
		entity.setDevelopmentIndex(resource.getDevelopmentIndex());
		return entity;
	}

	public Subdivision2 updateEntity(Subdivision2 entity, Subdivision2Resource resource) {
		entity.setName(resource.getName());
		entity.setSubdivision1(subdivision1Repository.findOne(resource.getSubdivision1().getId()));
		entity.setDevelopmentIndex(resource.getDevelopmentIndex());
		return entity;
	}

}
