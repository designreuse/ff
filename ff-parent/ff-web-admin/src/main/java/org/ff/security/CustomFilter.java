package org.ff.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.ff.security.AppUser.AppUserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			log.trace("User [{}] already authenticated", context.getAuthentication().getName());
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			Authentication authentication = null;
			if (httpRequest.getUserPrincipal() != null) {
				log.trace("Authenticating user [{}]...", httpRequest.getUserPrincipal().getName());
				authentication = new UsernamePasswordAuthenticationToken(
						httpRequest.getUserPrincipal().getName(), null, getGrantedAuthorities(httpRequest));
			} else {
				log.trace("Authenticating guest user...");
				authentication = new UsernamePasswordAuthenticationToken(
						"guest", null, AuthorityUtils.createAuthorityList(AppUserRole.ROLE_GUEST.name()));
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.trace("Initializing [{}]", this.getClass().getSimpleName());
	}

	@Override
	public void destroy() {
		log.trace("Destroying [{}]", this.getClass().getSimpleName());
	}

	private List<GrantedAuthority> getGrantedAuthorities(HttpServletRequest httpRequest) {
		if (httpRequest.isUserInRole(AppUserRole.ROLE_ADMIN.name())) {
			return AuthorityUtils.createAuthorityList(AppUserRole.ROLE_ADMIN.name());
		} else {
			return AuthorityUtils.createAuthorityList();
		}
	}

}
