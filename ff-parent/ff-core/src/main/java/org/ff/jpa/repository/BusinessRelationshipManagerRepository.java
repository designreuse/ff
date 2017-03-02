package org.ff.jpa.repository;

import org.ff.jpa.domain.BusinessRelationshipManager;
import org.ff.jpa.domain.OrganizationalUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BusinessRelationshipManagerRepository extends CrudRepository<BusinessRelationshipManager, Integer>, JpaSpecificationExecutor<BusinessRelationshipManager> {

	Page<BusinessRelationshipManager> findAll(Pageable pageable);

	BusinessRelationshipManager findByEmail(String email);

	@Modifying
	@Query("UPDATE BusinessRelationshipManager brm SET brm.organizationalUnit = NULL WHERE brm.organizationalUnit = :organizationalUnit")
	void nullifyOrganizationalUnit(@Param("organizationalUnit") OrganizationalUnit organizationalUnit);

}
