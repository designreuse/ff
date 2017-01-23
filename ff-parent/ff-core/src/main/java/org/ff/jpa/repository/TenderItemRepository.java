package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.TenderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TenderItemRepository extends CrudRepository<TenderItem, Integer>, JpaSpecificationExecutor<TenderItem> {

	Page<TenderItem> findAll(Pageable pageable);

	List<TenderItem> findByTender(Tender tender);

	List<TenderItem> findByItem(Item item);

	TenderItem findByTenderAndItem(Tender tender, Item item);

}
