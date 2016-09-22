package org.ff.jpa.repository;

import org.ff.jpa.domain.County;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CountyRepository extends CrudRepository<County, Integer>, JpaSpecificationExecutor<County> {

	Page<County> findAll(Pageable pageable);

	County findByName(String name);

}
