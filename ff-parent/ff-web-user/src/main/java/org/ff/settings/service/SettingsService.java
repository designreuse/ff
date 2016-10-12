package org.ff.settings.service;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.user.UserResourceAssembler;
import org.ff.settings.resource.SettingsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SettingsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Transactional
	public SettingsResource find(UserDetails principal) {
		User user = userRepository.findByEmail(principal.getUsername());

		SettingsResource resource = new SettingsResource();
		resource.setUser(userResourceAssembler.toResource(user, true));

		return resource;
	}

	@Transactional
	public SettingsResource save(UserDetails principal, SettingsResource resource) {
		log.debug("Saving settings [{}] for user [{}]", resource, principal.getUsername());

		User user = userRepository.findByEmail(principal.getUsername());

		user.setFirstName(resource.getUser().getFirstName());
		user.setLastName(resource.getUser().getLastName());

		if (StringUtils.isNotBlank(resource.getUser().getPassword())) {
			user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword(resource.getUser().getPassword(), null));
		}

		userRepository.save(user);

		resource.setUser(userResourceAssembler.toResource(user, true));

		return resource;
	}

}
