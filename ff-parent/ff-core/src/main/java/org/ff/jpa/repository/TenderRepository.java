package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TenderRepository extends CrudRepository<Tender, Integer>, JpaSpecificationExecutor<Tender> {

	Page<Tender> findAll(Pageable pageable);

	List<Tender> findByStatus(TenderStatus status);

	@Query("select count(*) from Tender t where t.status = :status")
	Long count(@Param("status") TenderStatus status);

	@Query("select count(*) from Tender t where t.creationDate between :start and :end)")
	Long count(@Param("start") DateTime start, @Param("end") DateTime end);

}
