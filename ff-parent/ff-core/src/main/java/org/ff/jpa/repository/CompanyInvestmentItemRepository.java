package org.ff.jpa.repository;

import org.ff.jpa.domain.CompanyInvestmentItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CompanyInvestmentItemRepository extends CrudRepository<CompanyInvestmentItem, Integer>, JpaSpecificationExecutor<CompanyInvestmentItem> {

}
