package org.ff.jpa.repository;

import org.ff.jpa.domain.ItemOption;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ItemOptionRepository extends CrudRepository<ItemOption, Integer>, JpaSpecificationExecutor<ItemOption> {

}
