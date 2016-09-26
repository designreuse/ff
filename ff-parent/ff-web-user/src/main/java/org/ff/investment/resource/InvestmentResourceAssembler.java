package org.ff.investment.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.image.resource.ImageResourceAssembler;
import org.ff.jpa.domain.Investment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestmentResourceAssembler {

	@Autowired
	private ImageResourceAssembler imageResourceAssembler;

	public InvestmentResource toResource(Investment entity) {
		InvestmentResource resource = new InvestmentResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setText(entity.getText());
		if (entity.getImage() != null) {
			resource.setImage(imageResourceAssembler.toResource(entity.getImage()));
		}
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		return resource;
	}

	public List<InvestmentResource> toResources(Iterable<Investment> entities) {
		List<InvestmentResource> resources = new ArrayList<>();
		for (Investment entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

}
