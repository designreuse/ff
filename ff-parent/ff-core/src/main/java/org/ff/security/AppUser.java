package org.ff.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class AppUser {

	public enum AppUserRole { ROLE_ADMIN, ROLE_OPERATOR_L1, ROLE_OPERATOR_L2, ROLE_OPERATOR_L3, ROLE_USER, ROLE_UNKNOWN }

	private String username;

	private String password;

	private boolean expired;

	private boolean locked;

	private boolean enabled;

	private boolean credentialsExpired;

	private List<String> roles;

	private String firstName;

	private String lastName;

}
