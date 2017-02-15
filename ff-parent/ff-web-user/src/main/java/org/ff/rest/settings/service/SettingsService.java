package org.ff.rest.settings.service;

import org.apache.commons.lang3.StringUtils;
import org.ff.common.password.PasswordService;
import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.settings.resource.SettingsResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
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
	public SettingsResource find(AppUserDetails principal) {
		User user = userRepository.findOne(principal.getUser().getId());

		SettingsResource resource = new SettingsResource();
		resource.setUser(userResourceAssembler.toResource(user, true));

		return resource;
	}

	@Transactional
	public SettingsResource save(AppUserDetails principal, SettingsResource resource) {
		log.debug("Saving settings [{}] for user [{}]", resource, principal.getUsername());

		User user = userRepository.findOne(principal.getUser().getId());

		user.setFirstName(resource.getUser().getFirstName());
		user.setLastName(resource.getUser().getLastName());
		user.setEmail(resource.getUser().getEmail());
		user.setEmail2(resource.getUser().getEmail2());

		if (StringUtils.isNotBlank(resource.getUser().getPassword())) {
			user.setPassword(PasswordService.encodePassword(resource.getUser().getPassword()));
		}

		userRepository.save(user);

		resource.setUser(userResourceAssembler.toResource(user, true));

		return resource;
	}

	@Transactional
	public SettingsResource deactivate(AppUserDetails principal) {
		User user = userRepository.findOne(principal.getUser().getId());

		user.setStatus(UserStatus.INACTIVE);
		userRepository.save(user);

		SettingsResource resource = new SettingsResource();
		resource.setUser(userResourceAssembler.toResource(user, true));

		return resource;
	}

}
