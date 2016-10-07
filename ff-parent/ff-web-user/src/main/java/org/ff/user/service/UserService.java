package org.ff.user.service;

import org.ff.jpa.domain.User;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.user.UserResource;
import org.ff.resource.user.UserResourceAssembler;
import org.ff.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService extends BaseService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserResourceAssembler resourceAssembler;

	@Transactional
	public ResponseEntity<?> register(UserResource resource) {
		log.debug("Registering user [{}]...", resource);

		User user = repository.findByEmail(resource.getEmail());
		if (user != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		user = repository.save(resourceAssembler.createEntity(resource));

		log.debug("Sending confirmation email to [{}]...", user.getEmail());
		// TODO

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
