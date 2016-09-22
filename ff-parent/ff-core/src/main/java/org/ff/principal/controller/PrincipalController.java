package org.ff.principal.controller;

import org.ff.principal.resource.PrincipalResource;
import org.ff.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrincipalController {

	@RequestMapping(method = RequestMethod.GET, value = { "/principal" })
	public PrincipalResource getPrincipal() {
		PrincipalResource resource = null;

		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication.getPrincipal() instanceof AppUserDetails) {
				AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();
				resource = new PrincipalResource();
				resource.setUsername(principal.getUsername());
			}
		}

		return resource;
	}

}
