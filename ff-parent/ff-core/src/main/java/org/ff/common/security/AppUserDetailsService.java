package org.ff.common.security;

import java.util.Arrays;

import org.ff.common.security.AppUser.AppUserRole;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Authenticating user with username [{}]", username);

		User user = userRepository.findByEmail(username);

		AppUser appUser = null;
		if (user != null && user.getStatus() == UserStatus.ACTIVE) {
			appUser = new AppUser(user.getEmail(), user.getPassword(), false, false, true, false, Arrays.asList(AppUserRole.ROLE_USER.name()), user.getFirstName(), user.getLastName(), user.getDemoUser());
		}

		if (appUser == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		return new AppUserDetails(appUser);
	}

}
