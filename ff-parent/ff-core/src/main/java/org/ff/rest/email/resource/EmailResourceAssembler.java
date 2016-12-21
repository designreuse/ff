package org.ff.rest.email.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailResourceAssembler {

	public EmailResource toResource(Email entity, boolean light) {
		EmailResource resource = new EmailResource();
		resource.setId(entity.getId());
		resource.setSubject(entity.getSubject());
		if (!light) {
			resource.setText(entity.getText());
		}
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<EmailResource> toResources(Iterable<Email> entities, boolean light) {
		List<EmailResource> resources = new ArrayList<>();
		for (Email entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

}
