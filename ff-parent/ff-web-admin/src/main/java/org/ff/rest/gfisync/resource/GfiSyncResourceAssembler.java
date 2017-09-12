package org.ff.rest.gfisync.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.GfiSync;
import org.springframework.stereotype.Component;

@Component
public class GfiSyncResourceAssembler {

	public GfiSyncResource toResource(GfiSync entity, boolean light) {
		GfiSyncResource resource = new GfiSyncResource();
		resource.setId(entity.getId());

		return resource;
	}

	public List<GfiSyncResource> toResources(Iterable<GfiSync> entities, boolean light) {
		List<GfiSyncResource> resources = new ArrayList<>();
		for (GfiSync entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

}
