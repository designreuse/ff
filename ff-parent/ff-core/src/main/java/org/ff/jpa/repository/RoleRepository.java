package org.ff.jpa.repository;

import org.ff.jpa.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

	Page<Role> findAll(Pageable pageable);

	Role findByName(String name);

}
