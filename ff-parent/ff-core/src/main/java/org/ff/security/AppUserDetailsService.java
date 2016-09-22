package org.ff.security;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.UserRepository;
import org.ff.security.AppUser.AppUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppUserDetailsService implements UserDetailsService {

	private static String ADMIN_USERNAME = "admin";
	private static String ADMIN_PASSWORD = "admin";

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Authenticating user with username [{}]", username);

		AppUser appUser = null;
		if (ADMIN_USERNAME.equals(username)) {
			appUser = new AppUser(username, DigestUtils.sha1Hex(ADMIN_PASSWORD), false, false, true, false, Arrays.asList(AppUserRole.ROLE_ADMIN.name()));
		} else {
			User user = userRepository.findByEmail(username);
			if (user != null) {
				appUser = new AppUser(user.getEmail(), DigestUtils.sha1Hex(user.getPassword()), false, false, true, false, Arrays.asList(AppUserRole.ROLE_USER.name()));
			}
		}

		if (appUser == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		return new AppUserDetails(appUser);
	}

}
