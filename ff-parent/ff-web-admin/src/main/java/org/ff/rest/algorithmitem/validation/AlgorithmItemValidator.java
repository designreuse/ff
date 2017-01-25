package org.ff.rest.algorithmitem.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ff.common.exception.ValidationFailedException;
import org.ff.jpa.domain.AlgorithmItem;
import org.ff.jpa.repository.AlgorithmItemRepository;
import org.ff.rest.algorithmitem.resource.AlgorithmItemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmItemValidator {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AlgorithmItemRepository repository;

	public void validate(AlgorithmItemResource resource, Locale locale) {
		List<String> messages = new ArrayList<>();

		AlgorithmItem entity = repository.findByCode(resource.getCode());
		if (entity != null && !entity.getId().equals(resource.getId())) {
			messages.add(messageSource.getMessage("item.code.exists", new Object[] { resource.getCode() }, locale));
		}

		if (!messages.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String message : messages) {
				sb.append("• ").append(message).append("<br>");
			}
			throw new ValidationFailedException(sb.toString());
		}

	}

}
