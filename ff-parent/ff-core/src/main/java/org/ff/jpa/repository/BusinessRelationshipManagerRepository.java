package org.ff.jpa.repository;

import org.ff.jpa.domain.BusinessRelationshipManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface BusinessRelationshipManagerRepository extends CrudRepository<BusinessRelationshipManager, Integer>, JpaSpecificationExecutor<BusinessRelationshipManager> {

	Page<BusinessRelationshipManager> findAll(Pageable pageable);

}
