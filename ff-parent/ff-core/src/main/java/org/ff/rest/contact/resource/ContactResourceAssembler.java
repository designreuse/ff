package org.ff.rest.contact.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactResourceAssembler {

	public ContactResource toResource(Contact entity, boolean light) {
		ContactResource resource = new ContactResource();
		resource.setId(entity.getId());
		resource.setCompanyName(entity.getCompanyName());
		resource.setCompanyCode(entity.getCompanyCode());
		resource.setName(entity.getName());
		resource.setEmail(entity.getEmail());
		resource.setPhone(entity.getPhone());
		resource.setTopic(entity.getTopic());
		resource.setType(entity.getType());
		resource.setChannel(entity.getChannel());
		resource.setText(entity.getText());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<ContactResource> toResources(List<Contact> entities, boolean light) {
		List<ContactResource> resources = new ArrayList<>();
		for (Contact entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

}
