package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Investment.InvestmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface InvestmentRepository extends CrudRepository<Investment, Integer>, JpaSpecificationExecutor<Investment> {

	Page<Investment> findAll(Pageable pageable);

	List<Investment> findByStatus(InvestmentStatus status);

}
