package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor<User> {

	Page<User> findAll(Pageable pageable);

	User findByEmail(String email);

	User findByRegistrationCode(String registrationCode);

	List<User> findByStatus(UserStatus status);

	User findByDemoUser(Boolean demoUser);

	Long countByDemoUser(Boolean demoUser);

	@Query("select count(*) from User u where u.registrationType = :registrationType")
	Long count(@Param("registrationType") UserRegistrationType registrationType);

	@Query("select count(*) from User u where u.creationDate between :start and :end)")
	Long count(@Param("start") DateTime start, @Param("end") DateTime end);

}
