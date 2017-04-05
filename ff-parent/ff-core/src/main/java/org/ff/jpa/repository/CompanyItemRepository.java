package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CompanyItemRepository extends CrudRepository<CompanyItem, Integer>, JpaSpecificationExecutor<CompanyItem> {

	List<CompanyItem> findByCompany(Company company);

	List<CompanyItem> findByItem(Item item);

	CompanyItem findByCompanyAndItem(Company company, Item item);

	Long deleteByItem(Item item);

}
