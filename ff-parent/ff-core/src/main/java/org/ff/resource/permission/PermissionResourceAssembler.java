package org.ff.resource.permission;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionResourceAssembler {

	public PermissionResource toResource(Permission entity) {
		PermissionResource resource = new PermissionResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		return resource;
	}

	public List<PermissionResource> toResources(Iterable<Permission> entities) {
		List<PermissionResource> resources = new ArrayList<>();
		for (Permission entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

	public Permission createEntity(PermissionResource resource) {
		Permission entity = new Permission();
		entity.setName(resource.getName());
		return entity;
	}

	public Permission updateEntity(Permission entity, PermissionResource resource) {
		entity.setName(resource.getName());
		return entity;
	}

}
