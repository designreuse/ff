package org.ff.jpa.repository;

import org.ff.jpa.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

	Page<Company> findAll(Pageable pageable);

	Company findByCode(String code);

}
