package org.ff.db.init;

import javax.annotation.PostConstruct;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitData {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@PostConstruct
	public void init() {
		User user = userRepository.findByDemoUser(Boolean.TRUE);
		if (user == null) {
			log.info("Creating demo user...");

			user = new User();
			user.setEmail("demo");
			user.setFirstName("Demo");
			user.setLastName("User");
			user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword("demo", null));
			user.setStatus(UserStatus.ACTIVE);
			user.setDemoUser(Boolean.TRUE);
			userRepository.save(user);

			Company company = new Company();
			company.setName("Demo Company");
			company.setUser(user);
			companyRepository.save(company);
		}
	}

}
