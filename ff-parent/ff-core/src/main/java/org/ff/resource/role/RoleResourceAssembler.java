package org.ff.resource.role;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.ff.jpa.domain.Permission;
import org.ff.jpa.domain.Role;
import org.ff.jpa.repository.PermissionRepository;
import org.ff.resource.permission.PermissionResource;
import org.ff.resource.permission.PermissionResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleResourceAssembler {

	@Autowired
	private PermissionResourceAssembler permissionResourceAssembler;

	@Autowired
	private PermissionRepository permissionRepository;

	public RoleResource toResource(Role entity, boolean light) {
		RoleResource resource = new RoleResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		if (!light) {
			resource.setPermissions(permissionResourceAssembler.toResources(entity.getPermissions()));
		}
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<RoleResource> toResources(Iterable<Role> entities, boolean light) {
		List<RoleResource> resources = new ArrayList<>();
		for (Role entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Role createEntity(RoleResource resource) {
		Role entity = new Role();
		entity.setName(resource.getName());

		if (entity.getPermissions() == null) {
			entity.setPermissions(new LinkedHashSet<Permission>());
		}
		entity.getPermissions().clear();

		if (resource.getPermissions() != null && !resource.getPermissions().isEmpty()) {
			for (PermissionResource permissionResource : resource.getPermissions()) {
				entity.getPermissions().add(permissionRepository.findOne(permissionResource.getId()));
			}
		}

		return entity;
	}

	public Role updateEntity(Role entity, RoleResource resource) {
		entity.setName(resource.getName());

		if (entity.getPermissions() == null) {
			entity.setPermissions(new LinkedHashSet<Permission>());
		}
		entity.getPermissions().clear();

		if (resource.getPermissions() != null && !resource.getPermissions().isEmpty()) {
			for (PermissionResource permissionResource : resource.getPermissions()) {
				entity.getPermissions().add(permissionRepository.findOne(permissionResource.getId()));
			}
		}

		return entity;
	}

}
