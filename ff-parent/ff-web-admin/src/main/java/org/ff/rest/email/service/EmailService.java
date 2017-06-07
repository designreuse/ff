package org.ff.rest.email.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.service.BaseService;
import org.ff.common.mailsender.MailSenderService;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.EmailRepository;
import org.ff.rest.email.resource.EmailResource;
import org.ff.rest.email.resource.EmailResourceAssembler;
import org.ff.rest.email.resource.SendEmailResource;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.usergroup.resource.UserGroupResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EmailRepository repository;

	@Autowired
	private EmailResourceAssembler resourceAssembler;

	@Autowired
	private MailSenderService mailSender;

	@Transactional(readOnly = true)
	public EmailResource find(Integer id, Locale locale) {
		Email entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.email", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	public void send(SendEmailResource resource) {
		log.info(resource.toString());

		Set<String> to = new HashSet<>();

		for (UserGroupResource userGroup : resource.getUserGroups()) {
			if (userGroup.getUsers() != null) {
				for (UserResource userResource : userGroup.getUsers()) {
					if (userResource.getStatus() != UserStatus.ACTIVE) {
						continue;
					}

					to.add(userResource.getEmail());

					if (StringUtils.isNotBlank(userResource.getEmail2())) {
						to.add(userResource.getEmail2());
					}
				}
			}
		}

		if (!to.isEmpty()) {
			mailSender.send(to.toArray(new String[to.size()]), resource.getSubject(), resource.getText());
		}
	}

}
