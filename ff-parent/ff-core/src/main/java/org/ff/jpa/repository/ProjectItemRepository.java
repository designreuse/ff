package org.ff.jpa.repository;

import org.ff.jpa.domain.ProjectItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ProjectItemRepository extends CrudRepository<ProjectItem, Integer>, JpaSpecificationExecutor<ProjectItem> {

}
