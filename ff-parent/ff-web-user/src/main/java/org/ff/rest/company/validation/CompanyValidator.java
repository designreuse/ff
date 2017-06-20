package org.ff.rest.company.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.ff.common.exception.ValidationFailedException;
import org.ff.jpa.domain.Company;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class CompanyValidator {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CompanyRepository companyRepository;

	public void validate(CompanyResource resource, Locale locale) {
		List<String> messages = new ArrayList<>();

		if (StringUtils.isBlank(resource.getCode())) {
			return;
		}

		Company entity = companyRepository.findByCode(resource.getCode());
		if (entity != null && !entity.getId().equals(resource.getId())) {
			messages.add(messageSource.getMessage("item.company.exists", new Object[] { resource.getCode() }, locale));
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
