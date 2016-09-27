package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TenderRepository extends CrudRepository<Tender, Integer>, JpaSpecificationExecutor<Tender> {

	Page<Tender> findAll(Pageable pageable);

	List<Tender> findByStatus(TenderStatus status);

}
