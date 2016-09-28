package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer>, JpaSpecificationExecutor<Item> {

	Page<Item> findAll(Pageable pageable);

	Item findByCodeAndEntityType(String code, ItemEntityType entityType);

	List<Item> findByEntityTypeAndStatusOrderByPosition(ItemEntityType entityType, ItemStatus status);

	List<Item> findByEntityType(ItemEntityType entityType);

}
