package org.ff.city.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.jpa.repository.CityRepository;
import org.ff.resource.city.CityResource;
import org.ff.resource.city.CityResourceAssembler;
import org.ff.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CityService extends BaseService {

	@Autowired
	private CityRepository repository;

	@Autowired
	private CityResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Cacheable("cities")
	@Transactional(readOnly = true)
	public List<CityResource> findAll() {
		log.debug("Finding cities...");

		List<CityResource> result = resourceAssembler.toResources(repository.findAll(), false);
		Collections.sort(result, new Comparator<CityResource>() {
			@Override
			public int compare(CityResource o1, CityResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

}
