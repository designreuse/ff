package org.ff.rest.activity.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityResourceAssembler {

	public ActivityResource toResource(Activity entity, boolean light) {
		ActivityResource resource = new ActivityResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<ActivityResource> toResources(Iterable<Activity> entities, boolean light) {
		List<ActivityResource> resources = new ArrayList<>();
		for (Activity entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Activity createEntity(ActivityResource resource) {
		Activity entity = new Activity();
		entity.setName(resource.getName());
		return entity;
	}

	public Activity updateEntity(Activity entity, ActivityResource resource) {
		entity.setName(resource.getName());
		return entity;
	}

}
