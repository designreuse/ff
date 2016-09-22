package org.ff.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff.security.AppUser.AppUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppAuthenticationResultHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	private static final String MESSAGE = "message";
	private static final String URL = "url";

	@Value("${server.context-path}")
	private String contextPath;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Map<String, String> data = new HashMap<>();
		if (isAdmin(authentication)) {
			data.put(MESSAGE, "Login success");
			data.put(URL, contextPath + "/#/dashboard/overview");
		} else if (isUser(authentication)) {
			data.put(MESSAGE, "Login success");
			data.put(URL, contextPath + "/#/articles/overview");
		} else {
			data.put(MESSAGE, "Invalid role");
		}
		objectMapper.writeValue(response.getOutputStream(), data);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		Map<String, String> data = new HashMap<>();
		data.put(MESSAGE, exception.getLocalizedMessage());
		objectMapper.writeValue(response.getOutputStream(), data);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
	}

	private boolean isAdmin(Authentication authentication) {
		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			if (grantedAuthority.getAuthority().equals(AppUserRole.ROLE_ADMIN.name())) {
				return true;
			}
		}
		return false;
	}

	private boolean isUser(Authentication authentication) {
		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			if (grantedAuthority.getAuthority().equals(AppUserRole.ROLE_USER.name())) {
				return true;
			}
		}
		return false;
	}

}
