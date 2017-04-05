package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.AlgorithmItem;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemStatus;
import org.ff.jpa.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AlgorithmItemRepository extends CrudRepository<AlgorithmItem, Integer>, JpaSpecificationExecutor<AlgorithmItem> {

	Page<AlgorithmItem> findAll(Pageable pageable);

	List<AlgorithmItem> findByStatusOrderByCode(AlgorithmItemStatus status);

	AlgorithmItem findByCode(String code);

	Long countByCompanyItem(Item companyItem);

	Long countByTenderItem(Item tenderItem);

}
