package org.ff.jpa.repository;

import org.ff.jpa.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Integer>, JpaSpecificationExecutor<Contact> {

	Page<Contact> findAll(Pageable pageable);

}
