package org.ff.rest.user.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ff.common.password.PasswordService;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.BusinessRelationshipManagerRepository;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.rest.businessrelationshipmanager.resource.BusinessRelationshipManagerResourceAssembler;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler {

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ProjectResourceAssembler projectResourceAssembler;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BusinessRelationshipManagerResourceAssembler businessRelationshipManagerResourceAssembler;

	@Autowired
	private BusinessRelationshipManagerRepository businessRelationshipManagerRepository;

	public UserResource toResource(User entity, boolean light) {
		UserResource resource = new UserResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setRegistrationType(entity.getRegistrationType());
		resource.setFirstName(entity.getFirstName());
		resource.setLastName(entity.getLastName());
		resource.setEmail(entity.getEmail());
		resource.setCompany((entity.getCompany() != null) ? companyResourceAssembler.toResource(entity.getCompany(), light) : null);
		resource.setProjects(projectResourceAssembler.toResources(projectRepository.findByCompany(entity.getCompany()), true));
		resource.setBusinessRelationshipManager((entity.getBusinessRelationshipManager() != null) ? businessRelationshipManagerResourceAssembler.toResource(entity.getBusinessRelationshipManager()) : null);
		resource.setBusinessRelationshipManagerSubstitute((entity.getBusinessRelationshipManagerSubstitute() != null) ? businessRelationshipManagerResourceAssembler.toResource(entity.getBusinessRelationshipManagerSubstitute()) : null);
		resource.setLastLoginDate(entity.getLastLoginDate().toDate());
		resource.setDemoUser(entity.getDemoUser());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<UserResource> toResources(Iterable<User> entities, boolean light) {
		List<UserResource> resources = new ArrayList<>();
		for (User entity : entities) {
			if (Boolean.TRUE == entity.getDemoUser()) {
				continue;
			}
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public User createEntity(UserResource resource) {
		User entity = new User();
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : User.UserStatus.INACTIVE);
		entity.setRegistrationType(resource.getRegistrationType());
		if (entity.getStatus() == UserStatus.WAITING_CONFIRMATION) {
			long now = System.currentTimeMillis();
			entity.setRegistrationCode(now + "-" + UUID.randomUUID().toString());
			entity.setRegistrationCodeSentDate(new DateTime(now));
		}
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		entity.setEmail(resource.getEmail());
		entity.setPassword(PasswordService.encodePassword(resource.getPassword()));
		Company company = null;
		if (resource.getCompany() != null) {
			company = companyResourceAssembler.createEntity(resource.getCompany());
			company.setUser(entity);
		}
		entity.setCompany(company);
		entity.setBusinessRelationshipManager((resource.getBusinessRelationshipManager() != null) ? businessRelationshipManagerRepository.findOne(entity.getBusinessRelationshipManager().getId()) : null);
		entity.setBusinessRelationshipManagerSubstitute((resource.getBusinessRelationshipManagerSubstitute() != null) ? businessRelationshipManagerRepository.findOne(entity.getBusinessRelationshipManagerSubstitute().getId()) : null);
		entity.setDemoUser((resource.getDemoUser() != null) ? resource.getDemoUser() : Boolean.FALSE);
		return entity;
	}

	public User updateEntity(User entity, UserResource resource) {
		entity.setStatus(resource.getStatus());
		entity.setRegistrationType(resource.getRegistrationType());
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		entity.setEmail(resource.getEmail());
		Company company = null;
		if (resource.getCompany() != null) {
			company = companyResourceAssembler.updateEntity(companyRepository.findOne(resource.getCompany().getId()), resource.getCompany());
			company.setUser(entity);
		}
		entity.setCompany(company);
		entity.setBusinessRelationshipManager((resource.getBusinessRelationshipManager() != null) ? businessRelationshipManagerRepository.findOne(entity.getBusinessRelationshipManager().getId()) : null);
		entity.setBusinessRelationshipManagerSubstitute((resource.getBusinessRelationshipManagerSubstitute() != null) ? businessRelationshipManagerRepository.findOne(entity.getBusinessRelationshipManagerSubstitute().getId()) : null);
		entity.setDemoUser((resource.getDemoUser() != null) ? resource.getDemoUser() : Boolean.FALSE);
		return entity;
	}

}
