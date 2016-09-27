package org.ff.resource.image;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageResourceAssembler {

	public ImageResource toResource(Image entity) {
		ImageResource resource = new ImageResource();
		resource.setId(entity.getId());
		resource.setBase64(entity.getBase64());
		return resource;
	}

	public List<ImageResource> toResources(List<Image> entities) {
		List<ImageResource> resources = new ArrayList<>();
		for (Image entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

}
