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

import org.apache.commons.lang3.StringUtils;
import org.ff.common.security.AppUser.AppUserRole;
import org.ff.jpa.domain.Role;
import org.ff.jpa.repository.RoleRepository;
import org.ff.zaba.sova.SovaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class SecurityFilter implements Filter {

	@Autowired
	private Environment environment;

	@Autowired
	private SovaClient sovaClient;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();

		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			log.trace("User [{}] already authorized", context.getAuthentication().getName());
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			Authentication authentication = null;

			if (httpRequest.getRequestURI().endsWith(".js")
					|| httpRequest.getRequestURI().endsWith(".css")
					|| httpRequest.getRequestURI().endsWith(".png")
					|| httpRequest.getRequestURI().endsWith(".woff")
					|| httpRequest.getRequestURI().endsWith(".woff2")) {
				// ignore these requests
			} else {
				log.debug("Request URI: {}", httpRequest.getRequestURI());

				if (httpRequest.getUserPrincipal() != null) {
					// when in security realm (e.g. WebSphere)
					log.debug("Authorizing user [{}]...", httpRequest.getUserPrincipal().getName());

					String roleExt = sovaClient.wsKorisnikAutorizacija(httpRequest.getUserPrincipal().getName());
					if (StringUtils.isNotBlank(roleExt)) {
						Role role = roleRepository.findByName(roleExt);
						if (role != null) {
							log.debug("Role [{}] recognized; autorization OK", role.getName());
							authentication = new UsernamePasswordAuthenticationToken(httpRequest.getUserPrincipal().getName(),
									null, getGrantedAuthority(role.getName()));
						} else {
							log.warn("Role [{}] not recognized; autorization NOK", roleExt);
							authentication = new UsernamePasswordAuthenticationToken(httpRequest.getUserPrincipal().getName(),
									null, AuthorityUtils.createAuthorityList(AppUserRole.ERROR_ROLE_NOT_RECOGNIZED.name()));
						}
					} else {
						log.warn("Role not found for user [{}]; autorization NOK", httpRequest.getUserPrincipal().getName());
						authentication = new UsernamePasswordAuthenticationToken(httpRequest.getUserPrincipal().getName(),
								null, AuthorityUtils.createAuthorityList(AppUserRole.ERROR_ROLE_NOT_FOUND.name()));
					}
				} else {
					for (String profile : environment.getActiveProfiles()) {
						// if development profile is active, use dummy authentication
						if (profile.startsWith("dev") || profile.startsWith("cloud")) {
							log.trace("Authorizing unknown user (dev/cloud env)...");
							authentication = new UsernamePasswordAuthenticationToken(
									"Administrator", null, AuthorityUtils.createAuthorityList(AppUserRole.ROLE_ADMIN.name()));
							break;
						}
					}
				}
			}

			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
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

	private List<GrantedAuthority> getGrantedAuthority(String role) {
		if (StringUtils.isNotBlank(role)) {
			return AuthorityUtils.createAuthorityList(role);
		} else {
			return AuthorityUtils.createAuthorityList(AppUserRole.ROLE_ADMIN.name());
		}
	}

}
