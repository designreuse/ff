package org.ff.resource.city;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.City;
import org.ff.jpa.repository.CountyRepository;
import org.ff.resource.county.CountyResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityResourceAssembler {

	@Autowired
	private CountyResourceAssembler countyResourceAssembler;

	@Autowired
	private CountyRepository countyRepository;

	public CityResource toResource(City entity, boolean light) {
		CityResource resource = new CityResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setCounty(countyResourceAssembler.toResource(entity.getCounty(), light));
		resource.setDevelopmentIndex(entity.getDevelopmentIndex());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<CityResource> toResources(Iterable<City> entities, boolean light) {
		List<CityResource> resources = new ArrayList<>();
		for (City entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public City createEntity(CityResource resource) {
		City entity = new City();
		entity.setName(resource.getName());
		entity.setCounty(countyRepository.findOne(resource.getCounty().getId()));
		entity.setDevelopmentIndex(resource.getDevelopmentIndex());
		return entity;
	}

	public City updateEntity(City entity, CityResource resource) {
		entity.setName(resource.getName());
		entity.setCounty(countyRepository.findOne(resource.getCounty().getId()));
		entity.setDevelopmentIndex(resource.getDevelopmentIndex());
		return entity;
	}

}
