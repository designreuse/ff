package org.ff.rest.settings.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ff.common.exception.ValidationFailedException;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.settings.resource.SettingsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class SettingsValidator {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository userRepository;

	public void validate(SettingsResource resource, Locale locale) {
		List<String> messages = new ArrayList<>();

		User entity = userRepository.findByEmail(resource.getUser().getEmail());
		if (entity != null && !entity.getId().equals(resource.getUser().getId())) {
			messages.add(messageSource.getMessage("user.email.exists", new Object[] { resource.getUser().getEmail() }, locale));
		}

		if (!messages.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String message : messages) {
				sb.append(message).append("<br>");
			}
			throw new ValidationFailedException(sb.toString());
		}

	}
}
