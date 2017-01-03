package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyInvestment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CompanyInvestmentRepository extends CrudRepository<CompanyInvestment, Integer>, JpaSpecificationExecutor<CompanyInvestment> {

	List<CompanyInvestment> findByCompany(Company company);

}
