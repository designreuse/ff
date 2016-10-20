package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.UserGroup;
import org.ff.jpa.domain.UserGroup.UserGroupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserGroupRepository extends CrudRepository<UserGroup, Integer>, JpaSpecificationExecutor<UserGroup> {

	Page<UserGroup> findAll(Pageable pageable);

	List<UserGroup> findByStatus(UserGroupStatus status);

}
