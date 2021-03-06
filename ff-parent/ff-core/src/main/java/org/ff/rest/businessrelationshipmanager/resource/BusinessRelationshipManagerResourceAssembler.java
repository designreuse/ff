package org.ff.rest.businessrelationshipmanager.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.BusinessRelationshipManager;
import org.ff.jpa.repository.OrganizationalUnitRepository;
import org.ff.rest.organizationalunit.resource.OrganizationalUnitResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessRelationshipManagerResourceAssembler {

	@Autowired
	private OrganizationalUnitResourceAssembler organizationalUnitResourceAssembler;

	@Autowired
	private OrganizationalUnitRepository organizationalUnitRepository;

	public BusinessRelationshipManagerResource toResource(BusinessRelationshipManager entity) {
		BusinessRelationshipManagerResource resource = new BusinessRelationshipManagerResource();
		resource.setId(entity.getId());
		resource.setFirstName(entity.getFirstName());
		resource.setLastName(entity.getLastName());
		resource.setPhone(entity.getPhone());
		resource.setMobile(entity.getMobile());
		resource.setEmail(entity.getEmail());
		resource.setOrganizationalUnit((entity.getOrganizationalUnit() != null) ? organizationalUnitResourceAssembler.toResource(entity.getOrganizationalUnit()) : null);
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<BusinessRelationshipManagerResource> toResources(Iterable<BusinessRelationshipManager> entities) {
		List<BusinessRelationshipManagerResource> resources = new ArrayList<>();
		for (BusinessRelationshipManager entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

	public BusinessRelationshipManager createEntity(BusinessRelationshipManagerResource resource) {
		BusinessRelationshipManager entity = new BusinessRelationshipManager();
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		entity.setPhone(resource.getPhone());
		entity.setMobile(resource.getMobile());
		entity.setEmail(resource.getEmail());
		entity.setOrganizationalUnit((resource.getOrganizationalUnit() != null) ? organizationalUnitRepository.findOne(resource.getOrganizationalUnit().getId()) : null);
		return entity;
	}

	public BusinessRelationshipManager updateEntity(BusinessRelationshipManager entity, BusinessRelationshipManagerResource resource) {
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		entity.setPhone(resource.getPhone());
		entity.setMobile(resource.getMobile());
		entity.setEmail(resource.getEmail());
		entity.setOrganizationalUnit((resource.getOrganizationalUnit() != null) ? organizationalUnitRepository.findOne(resource.getOrganizationalUnit().getId()) : null);
		return entity;
	}

}
