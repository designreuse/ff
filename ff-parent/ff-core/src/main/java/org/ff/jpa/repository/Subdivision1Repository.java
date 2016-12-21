package org.ff.jpa.repository;

import org.ff.jpa.domain.Subdivision1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface Subdivision1Repository extends CrudRepository<Subdivision1, Integer>, JpaSpecificationExecutor<Subdivision1> {

	Page<Subdivision1> findAll(Pageable pageable);

	Subdivision1 findByName(String name);

}
