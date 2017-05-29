package org.ff.rest.principal.controller;

import java.util.ArrayList;
import java.util.List;

import org.ff.common.security.AppUser.AppUserRole;
import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Permission;
import org.ff.jpa.domain.Role;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.PermissionRepository;
import org.ff.jpa.repository.RoleRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.principal.resource.PrincipalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrincipalController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@RequestMapping(method = RequestMethod.GET, value = { "/principal" })
	public PrincipalResource getPrincipal() {
		PrincipalResource resource = null;

		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication.getPrincipal() instanceof AppUserDetails) {
				// user application
				AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();

				User user = userRepository.findOne(principal.getUser().getId());
				principal.getUser().setUsername(user.getEmail());

				String roleName = principal.getAuthorities().iterator().next().toString();
				List<String> permissions = getPermissions(roleName);

				resource = new PrincipalResource(principal.getUsername(), roleName, permissions, principal.getUser().getFirstName(),
						principal.getUser().getLastName(), principal.getUser().getDemoUser(), principal.getUser().getRegistrationType());
			} else if (authentication instanceof UsernamePasswordAuthenticationToken) {
				// admin application
				UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) authentication;

				String roleName = principal.getAuthorities().iterator().next().toString();
				List<String> permissions = getPermissions(roleName);

				resource = new PrincipalResource(principal.getName(), roleName, permissions, null, null, null, null);
			}
		}

		return resource;
	}

	private List<String> getPermissions(String roleName) {
		List<String> permissions = new ArrayList<>();

		if (roleName.equals(AppUserRole.ROLE_ADMIN.name())) {
			for (Permission permission : permissionRepository.findAll()) {
				permissions.add(permission.getName());
			}
		} else {
			Role role = roleRepository.findByName(roleName);
			if (role != null) {
				for (Permission permission : role.getPermissions()) {
					permissions.add(permission.getName());
				}
			}
		}

		return permissions;
	}

}
