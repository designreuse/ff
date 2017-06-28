package org.ff.rest.email.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.service.BaseService;
import org.ff.common.mailsender.MailSenderResource;
import org.ff.common.mailsender.MailSenderService;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.domain.UserEmail;
import org.ff.jpa.repository.EmailRepository;
import org.ff.jpa.repository.UserEmailRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.email.resource.EmailResource;
import org.ff.rest.email.resource.EmailResourceAssembler;
import org.ff.rest.email.resource.SendEmailResource;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.usergroup.resource.UserGroupResource;
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

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private UserEmailRepository userEmailRepository;

	@Transactional(readOnly = true)
	public EmailResource find(Integer id, Locale locale) {
		Email entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.email", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	/**
	 * Method sends e-mail to a group of users.
	 * @param resource
	 */
	@Transactional
	public void send(SendEmailResource resource) {
		Email email = new Email();
		email.setSubject(resource.getSubject());
		email.setText(resource.getText());
		emailRepository.save(email);

		List<MailSenderResource> mailSenderResources = new ArrayList<>();

		for (UserGroupResource userGroup : resource.getUserGroups()) {
			if (userGroup.getUsers() != null) {
				for (UserResource userResource : userGroup.getUsers()) {
					if (userResource.getStatus() != UserStatus.ACTIVE || StringUtils.isBlank(userResource.getEmail())) {
						continue;
					}

					User user = userRepository.findOne(userResource.getId());

					UserEmail userEmail = new UserEmail();
					userEmail.setEmail(email);
					userEmail.setUser(user);
					userEmailRepository.save(userEmail);

					mailSenderResources.add(new MailSenderResource(user.getEmail(), StringUtils.isNotBlank(user.getEmail2()) ? user.getEmail2() : null, resource.getSubject(), resource.getText()));

					if (user.getBusinessRelationshipManager() != null && StringUtils.isNotBlank(user.getBusinessRelationshipManager().getEmail())) {
						Map<String, Object> model = new HashMap<String, Object>();
						model.put("originalEmailText", resource.getText());
						model.put("user", user.getEmail());
						mailSenderResources.add(new MailSenderResource(user.getBusinessRelationshipManager().getEmail(), (user.getBusinessRelationshipManagerSubstitute() != null && StringUtils.isNotBlank((user.getBusinessRelationshipManagerSubstitute().getEmail()))) ? user.getBusinessRelationshipManagerSubstitute().getEmail() : null, resource.getSubject(), mailSender.processTemplateIntoString("email_user_brm.ftl", model)));
					}
				}
			}
		}

		mailSender.send(mailSenderResources);
	}

}
