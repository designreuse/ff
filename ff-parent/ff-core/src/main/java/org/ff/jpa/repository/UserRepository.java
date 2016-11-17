package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor<User> {

	Page<User> findAll(Pageable pageable);

	User findByEmail(String email);

	User findByRegistrationCode(String registrationCode);

	List<User> findByStatus(UserStatus status);

	User findByDemoUser(Boolean demoUser);

}
