package org.ff.jpa.repository;

import org.ff.jpa.domain.User;
import org.ff.jpa.domain.UserEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserEmailRepository extends CrudRepository<UserEmail, Integer>, JpaSpecificationExecutor<UserEmail> {

	Page<UserEmail> findAll(Pageable pageable);

	Long deleteByUser(User user);

}
