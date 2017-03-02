package org.ff.rest.organizationalunit.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.OrganizationalUnit;
import org.springframework.stereotype.Component;

@Component
public class OrganizationalUnitResourceAssembler {

	public OrganizationalUnitResource toResource(OrganizationalUnit entity) {
		OrganizationalUnitResource resource = new OrganizationalUnitResource();
		resource.setId(entity.getId());
		resource.setCode(entity.getCode());
		resource.setName(entity.getName());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<OrganizationalUnitResource> toResources(Iterable<OrganizationalUnit> entities) {
		List<OrganizationalUnitResource> resources = new ArrayList<>();
		for (OrganizationalUnit entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

	public OrganizationalUnit createEntity(OrganizationalUnitResource resource) {
		OrganizationalUnit entity = new OrganizationalUnit();
		entity.setCode(resource.getCode());
		entity.setName(resource.getName());
		return entity;
	}

	public OrganizationalUnit updateEntity(OrganizationalUnit entity, OrganizationalUnitResource resource) {
		entity.setCode(resource.getCode());
		entity.setName(resource.getName());
		return entity;
	}

}
