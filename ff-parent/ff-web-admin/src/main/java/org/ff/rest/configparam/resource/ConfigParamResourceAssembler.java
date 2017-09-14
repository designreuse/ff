package org.ff.rest.configparam.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.ConfigParam;
import org.springframework.stereotype.Component;

@Component
public class ConfigParamResourceAssembler {

	public ConfigParamResource toResource(ConfigParam entity) {
		ConfigParamResource resource = new ConfigParamResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setValue(entity.getValue());
		resource.setDescription(entity.getDescription());
		return resource;
	}

	public List<ConfigParamResource> toResources(Iterable<ConfigParam> entities) {
		List<ConfigParamResource> resources = new ArrayList<>();
		for (ConfigParam entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

	public ConfigParam updateEntity(ConfigParam entity, ConfigParamResource resource) {
		entity.setName(resource.getName());
		entity.setValue(resource.getValue());
		entity.setDescription(resource.getDescription());
		return entity;
	}

}
