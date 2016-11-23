package org.ff.jpa.repository;

import org.ff.jpa.domain.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

	Page<Permission> findAll(Pageable pageable);

}
