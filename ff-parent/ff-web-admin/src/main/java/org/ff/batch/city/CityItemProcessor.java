package org.ff.batch.city;

import org.ff.jpa.domain.City;
import org.ff.jpa.domain.County;
import org.ff.jpa.repository.CityRepository;
import org.ff.jpa.repository.CountyRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class CityItemProcessor implements ItemProcessor<CityItem, City> {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountyRepository countyRepository;

	@Override
	public City process(CityItem item) throws Exception {
		if (cityRepository.findOne(item.getId()) == null) {
			City entity = new City();
			entity.setId(item.getId());
			entity.setName(item.getName());
			entity.setCounty(getCounty(item));
			entity.setDevelopmentIndex(item.getGroup());
			return entity;
		} else {
			return null;
		}
	}

	private County getCounty(CityItem item) throws Exception {
		County entity = countyRepository.findByName(item.getCounty());
		if (entity == null) {
			entity = new County();
			entity.setName(item.getCounty());
		}
		return entity;
	}

}
