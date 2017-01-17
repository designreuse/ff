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

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			log.trace("User [{}] already authenticated", context.getAuthentication().getName());
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			Authentication authentication = null;
			if (httpRequest.getUserPrincipal() != null) {
				// when in security realm (e.g. WebSphere)
				log.trace("Authenticating user [{}]...", httpRequest.getUserPrincipal().getName());
				authentication = new UsernamePasswordAuthenticationToken(httpRequest.getUserPrincipal().getName(),
						null, getGrantedAuthority(sovaClient.wsKorisnikAutorizacija(httpRequest.getUserPrincipal().getName())));
			} else {
				for (String profile : environment.getActiveProfiles()) {
					// if development profile is active, use dummy authentication
					if (profile.startsWith("dev")) {
						log.trace("Authenticating unknown user (dev env)...");
						authentication = new UsernamePasswordAuthenticationToken(
								"Administrator", null, AuthorityUtils.createAuthorityList(AppUserRole.ROLE_ADMIN.name()));
						break;
					}
				}
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

	private List<GrantedAuthority> getGrantedAuthority(String role) {
		if (StringUtils.isNotBlank(role)) {
			return AuthorityUtils.createAuthorityList(role);
		} else {
			return AuthorityUtils.createAuthorityList(AppUserRole.ROLE_ADMIN.name());
		}
	}

}
