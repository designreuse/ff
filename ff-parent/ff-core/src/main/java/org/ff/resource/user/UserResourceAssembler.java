package org.ff.resource.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.resource.businessrelationshipmanager.BusinessRelationshipManagerResourceAssembler;
import org.ff.resource.company.CompanyResourceAssembler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler {

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private BusinessRelationshipManagerResourceAssembler businessRelationshipManagerResourceAssembler;

	public UserResource toResource(User entity, boolean light) {
		UserResource resource = new UserResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setFirstName(entity.getFirstName());
		resource.setLastName(entity.getLastName());
		resource.setEmail(entity.getEmail());
		resource.setCompany((entity.getCompany() != null) ? companyResourceAssembler.toResource(entity.getCompany(), light) : null);
		resource.setBusinessRelationshipManager((entity.getBusinessRelationshipManager() != null) ? businessRelationshipManagerResourceAssembler.toResource(entity.getBusinessRelationshipManager()) : null);
		resource.setLastLoginDate(entity.getLastLoginDate().toDate());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<UserResource> toResources(Iterable<User> entities, boolean light) {
		List<UserResource> resources = new ArrayList<>();
		for (User entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public User createEntity(UserResource resource) {
		User entity = new User();
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : User.UserStatus.INACTIVE);
		if (entity.getStatus() == UserStatus.WAITING_CONFIRMATION) {
			long now = System.currentTimeMillis();
			entity.setRegistrationCode(now + "-" + UUID.randomUUID().toString());
			entity.setRegistrationCodeSentDate(new DateTime(now));
		}
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		entity.setEmail(resource.getEmail());
		entity.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword(resource.getPassword(), null));
		Company company = null;
		if (resource.getCompany() != null) {
			company = companyResourceAssembler.createEntity(resource.getCompany());
			company.setUser(entity);
		}
		entity.setCompany(company);
		return entity;
	}

	public User updateEntity(User entity, UserResource resource) {
		entity.setStatus(resource.getStatus());
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		entity.setEmail(resource.getEmail());
		Company company = null;
		if (resource.getCompany() != null) {
			company = companyResourceAssembler.updateEntity(companyRepository.findOne(resource.getCompany().getId()), resource.getCompany());
			company.setUser(entity);
		}
		entity.setCompany(company);
		return entity;
	}

}
