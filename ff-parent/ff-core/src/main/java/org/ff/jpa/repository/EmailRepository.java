package org.ff.jpa.repository;

import org.ff.jpa.domain.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, Integer>, JpaSpecificationExecutor<Email> {

	Page<Email> findAll(Pageable pageable);

}
