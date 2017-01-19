package org.ff.common.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImpressionRepository impressionRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User user = userRepository.findByEmail(authentication.getName());
		if (user != null) {
			user.setLastLoginDate(new DateTime());
			userRepository.save(user);

			// record user visit (login)
			Impression impression = new Impression();
			impression.setEntityType(EntityType.USER);
			impression.setEntityId(user.getId());
			impressionRepository.save(impression);
		}

		Map<String, String> data = new HashMap<>();
		data.put(MESSAGE, "Login success");
		data.put(URL, contextPath + "/#/tenders/overview");
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

}
