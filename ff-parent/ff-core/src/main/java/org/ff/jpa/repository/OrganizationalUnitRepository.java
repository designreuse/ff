package org.ff.jpa.repository;

import org.ff.jpa.domain.OrganizationalUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationalUnitRepository extends CrudRepository<OrganizationalUnit, Integer>, JpaSpecificationExecutor<OrganizationalUnit> {

	Page<OrganizationalUnit> findAll(Pageable pageable);

	OrganizationalUnit findByCode(String code);

}
