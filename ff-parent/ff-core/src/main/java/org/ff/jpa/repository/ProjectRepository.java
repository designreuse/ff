package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

	List<Project> findByCompany(Company company);

	Integer countByCompany(Company company);

	Long deleteByCompany(Company company);

}
