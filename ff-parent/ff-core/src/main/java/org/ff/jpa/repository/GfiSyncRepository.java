package org.ff.jpa.repository;

import org.ff.jpa.domain.GfiSync;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface GfiSyncRepository extends CrudRepository<GfiSync, Integer>, JpaSpecificationExecutor<GfiSync> {

	Page<GfiSync> findAll(Pageable pageable);

}
