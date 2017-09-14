package org.ff.rest.configparam.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.jpa.domain.ConfigParam;
import org.ff.jpa.domain.ConfigParam.ConfigParamName;
import org.ff.jpa.repository.ConfigParamRepository;
import org.ff.rest.configparam.resource.ConfigParamResource;
import org.ff.rest.configparam.resource.ConfigParamResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigParamService {

	@Autowired
	private ConfigParamRepository repository;

	@Autowired
	private ConfigParamResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<ConfigParamResource> findAll() {
		List<ConfigParamResource> resources = new ArrayList<>();

		for (ConfigParam entity : repository.findAll()) {
			if (ConfigParamName.valueOf(entity.getName()) == ConfigParamName.test_mode
					|| ConfigParamName.valueOf(entity.getName()) == ConfigParamName.sendgrid_enabled
					|| ConfigParamName.valueOf(entity.getName()) == ConfigParamName.sendgrid_apikey) {
				continue;
			}
			resources.add(resourceAssembler.toResource(entity));
		}

		Collections.sort(resources, new Comparator<ConfigParamResource>() {
			@Override
			public int compare(ConfigParamResource o1, ConfigParamResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return resources;
	}

	@Transactional
	public List<ConfigParamResource> save(List<ConfigParamResource> resources) {
		List<ConfigParam> entities = new ArrayList<>();
		for (ConfigParamResource resource : resources) {
			entities.add(resourceAssembler.updateEntity(repository.findOne(resource.getId()), resource));
		}
		repository.save(entities);

		return findAll();
	}


}
