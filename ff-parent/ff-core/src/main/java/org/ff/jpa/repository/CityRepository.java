package org.ff.jpa.repository;

import org.ff.jpa.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Integer>, JpaSpecificationExecutor<City> {

	Page<City> findAll(Pageable pageable);

}
