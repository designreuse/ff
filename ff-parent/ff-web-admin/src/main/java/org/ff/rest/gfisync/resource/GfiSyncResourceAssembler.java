package org.ff.rest.gfisync.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.GfiSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GfiSyncResourceAssembler {

	@Autowired
	private GfiSyncErrorResourceAssembler gfiSyncErrorResourceAssembler;

	public GfiSyncResource toResource(GfiSync entity, boolean light) {
		GfiSyncResource resource = new GfiSyncResource();
		resource.setId(entity.getId());
		resource.setCntTotal(entity.getCntTotal());
		resource.setCntOk(entity.getCntOk());
		resource.setCntNok(entity.getCntNok());
		resource.setStartTime(entity.getStartTime().toDate());
		resource.setEndTime(entity.getEndTime().toDate());
		if (!light) {
			resource.setErrors(gfiSyncErrorResourceAssembler.toResources(entity.getErrors(), true));
		}
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
