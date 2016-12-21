package org.ff.rest.usergroup.resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.ff.jpa.domain.User;
import org.ff.jpa.domain.UserGroup;
import org.ff.jpa.domain.UserGroup.UserGroupStatus;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGroupResourceAssembler {

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private UserRepository userRepository;

	public UserGroupResource toResource(UserGroup entity, boolean light) {
		UserGroupResource resource = new UserGroupResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setName(entity.getName());
		resource.setUsers(userResourceAssembler.toResources(entity.getUsers(), true));
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<UserGroupResource> toResources(Iterable<UserGroup> entities, boolean light) {
		List<UserGroupResource> resources = new ArrayList<>();
		for (UserGroup entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public UserGroup createEntity(UserGroupResource resource) {
		UserGroup entity = new UserGroup();
		entity.setName(resource.getName());
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : UserGroupStatus.INACTIVE);

		if (entity.getUsers() == null) {
			entity.setUsers(new LinkedHashSet<User>());
		}
		entity.getUsers().clear();

		if (resource.getUsers() != null && !resource.getUsers().isEmpty()) {
			for (UserResource userResource : resource.getUsers()) {
				entity.getUsers().add(userRepository.findOne(userResource.getId()));
			}
		}

		return entity;
	}

	public UserGroup updateEntity(UserGroup entity, UserGroupResource resource) {
		entity.setName(resource.getName());
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : UserGroupStatus.INACTIVE);

		if (entity.getUsers() == null) {
			entity.setUsers(new LinkedHashSet<User>());
		}
		entity.getUsers().clear();

		if (resource.getUsers() != null && !resource.getUsers().isEmpty()) {
			for (UserResource userResource : resource.getUsers()) {
				entity.getUsers().add(userRepository.findOne(userResource.getId()));
			}
		}

		return entity;
	}

}
