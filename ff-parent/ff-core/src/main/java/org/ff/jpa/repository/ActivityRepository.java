package org.ff.jpa.repository;

import org.ff.jpa.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {

	Page<Activity> findAll(Pageable pageable);

}
