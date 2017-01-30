package org.ff.common.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class AppUser {

	public enum AppUserRole { ROLE_USER, ROLE_ADMIN, ERROR_ROLE_NOT_FOUND, ERROR_ROLE_NOT_RECOGNIZED }

	private String username;

	private String password;

	private boolean expired;

	private boolean locked;

	private boolean enabled;

	private boolean credentialsExpired;

	private List<String> roles;

	private String firstName;

	private String lastName;

	private Boolean demoUser;

}
