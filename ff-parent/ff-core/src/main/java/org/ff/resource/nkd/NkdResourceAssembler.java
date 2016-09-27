package org.ff.resource.nkd;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Nkd;
import org.springframework.stereotype.Component;

@Component
public class NkdResourceAssembler {

	public NkdResource toResource(Nkd entity, boolean light) {
		NkdResource resource = new NkdResource();
		resource.setId(entity.getId());
		resource.setSector(entity.getSector());
		resource.setSectorName(entity.getSectorName());
		resource.setArea(entity.getArea());
		resource.setActivity(entity.getActivity());
		resource.setActivityName(entity.getActivityName());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<NkdResource> toResources(Iterable<Nkd> entities, boolean light) {
		List<NkdResource> resources = new ArrayList<>();
		for (Nkd entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Nkd createEntity(NkdResource resource) {
		Nkd entity = new Nkd();
		entity.setSector(resource.getSector());
		entity.setSectorName(resource.getSectorName());
		entity.setArea(resource.getArea());
		entity.setActivity(resource.getActivity());
		entity.setActivityName(resource.getActivityName());
		return entity;
	}

	public Nkd updateEntity(Nkd entity, NkdResource resource) {
		entity.setSector(resource.getSector());
		entity.setSectorName(resource.getSectorName());
		entity.setArea(resource.getArea());
		entity.setActivity(resource.getActivity());
		entity.setActivityName(resource.getActivityName());
		return entity;
	}

}
