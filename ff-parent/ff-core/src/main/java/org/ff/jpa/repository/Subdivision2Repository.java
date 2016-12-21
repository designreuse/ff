package org.ff.jpa.repository;

import org.ff.jpa.domain.Subdivision2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface Subdivision2Repository extends CrudRepository<Subdivision2, Integer>, JpaSpecificationExecutor<Subdivision2> {

	Page<Subdivision2> findAll(Pageable pageable);

	Subdivision2 findByName(String name);

}
