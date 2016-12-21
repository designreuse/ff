package org.ff.rest.item.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ff.common.exception.ValidationFailedException;
import org.ff.jpa.domain.Item;
import org.ff.jpa.repository.ItemRepository;
import org.ff.rest.item.resource.ItemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ItemValidator {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ItemRepository itemRepository;

	public void validate(ItemResource resource, Locale locale) {
		List<String> messages = new ArrayList<>();

		Item entity = itemRepository.findByCodeAndEntityType(resource.getCode(), resource.getEntityType());
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
