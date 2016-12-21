package org.ff.rest.email.service;

import java.util.Locale;

import org.ff.base.service.BaseService;
import org.ff.jpa.domain.Email;
import org.ff.jpa.repository.EmailRepository;
import org.ff.rest.email.resource.EmailResource;
import org.ff.rest.email.resource.EmailResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EmailRepository repository;

	@Autowired
	private EmailResourceAssembler resourceAssembler;

	@Transactional(readOnly = true)
	public EmailResource find(Integer id, Locale locale) {
		Email entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.email", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

}
