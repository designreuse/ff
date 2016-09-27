package org.ff.resource.investment;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Image;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.repository.ImageRepository;
import org.ff.resource.image.ImageResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestmentResourceAssembler {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ImageResourceAssembler imageResourceAssembler;

	public InvestmentResource toResource(Investment entity, boolean light) {
		InvestmentResource resource = new InvestmentResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setName(entity.getName());
		if (!light) {
			resource.setText(entity.getText());
		}
		if (!light && entity.getImage() != null) {
			resource.setImage(imageResourceAssembler.toResource(entity.getImage()));
		}
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<InvestmentResource> toResources(Iterable<Investment> entities, boolean light) {
		List<InvestmentResource> resources = new ArrayList<>();
		for (Investment entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Investment createEntity(InvestmentResource resource) {
		Investment entity = new Investment();
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : Investment.InvestmentStatus.INACTIVE);
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		Image image = new Image();
		if (resource.getImage() != null) {
			image.setBase64(resource.getImage().getBase64());
		}
		entity.setImage(imageRepository.save(image));
		return entity;
	}

	public Investment updateEntity(Investment entity, InvestmentResource resource) {
		entity.setStatus(resource.getStatus());
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		if (resource.getImage() != null) {
			Image image = imageRepository.findOne(resource.getImage().getId());
			image.setBase64(resource.getImage().getBase64());
			imageRepository.save(image);
			entity.setImage(image);
		}
		return entity;
	}

}
