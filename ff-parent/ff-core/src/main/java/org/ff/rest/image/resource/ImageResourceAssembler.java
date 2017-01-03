package org.ff.rest.image.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageResourceAssembler {

	public ImageResource toResource(Image entity, boolean light) {
		ImageResource resource = new ImageResource();
		resource.setId(entity.getId());
		if (!light) {
			resource.setBase64(entity.getBase64());
		}
		return resource;
	}

	public List<ImageResource> toResources(List<Image> entities, boolean light) {
		List<ImageResource> resources = new ArrayList<>();
		for (Image entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

}
