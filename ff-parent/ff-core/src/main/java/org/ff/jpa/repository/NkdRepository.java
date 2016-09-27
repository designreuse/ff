package org.ff.jpa.repository;

import org.ff.jpa.domain.Nkd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface NkdRepository extends CrudRepository<Nkd, Integer>, JpaSpecificationExecutor<Nkd> {

	Page<Nkd> findAll(Pageable pageable);

}
