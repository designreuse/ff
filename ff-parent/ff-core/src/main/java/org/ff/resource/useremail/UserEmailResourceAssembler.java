package org.ff.resource.useremail;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.UserEmail;
import org.ff.resource.email.EmailResourceAssembler;
import org.ff.resource.tender.TenderResourceAssembler;
import org.ff.resource.user.UserResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEmailResourceAssembler {

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private EmailResourceAssembler emailResourceAssembler;

	public UserEmailResource toResource(UserEmail entity, boolean light) {
		UserEmailResource resource = new UserEmailResource();
		resource.setId(entity.getId());
		resource.setUser((entity.getUser() != null) ? userResourceAssembler.toResource(entity.getUser(), light) : null);
		resource.setTender((entity.getTender() != null) ? tenderResourceAssembler.toResource(entity.getTender(), light) : null);
		resource.setEmail((entity.getEmail() != null) ? emailResourceAssembler.toResource(entity.getEmail(), light) : null);
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<UserEmailResource> toResources(Iterable<UserEmail> entities, boolean light) {
		List<UserEmailResource> resources = new ArrayList<>();
		for (UserEmail entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

}
