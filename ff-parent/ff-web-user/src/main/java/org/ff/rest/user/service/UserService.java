package org.ff.rest.user.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.ff.base.properties.BaseProperties;
import org.ff.base.service.BaseService;
import org.ff.common.mailsender.MailSenderService;
import org.ff.common.password.PasswordService;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserResourceAssembler resourceAssembler;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private Configuration configuration;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private PasswordService passwordService;

	@Transactional
	public ResponseEntity<?> register(UserResource resource, Locale locale) {
		log.debug("Registering user [{}]...", resource);

		User user = repository.findByEmail(resource.getEmail());
		if (user != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		resource.setRegistrationType(UserRegistrationType.INTERNAL);
		user = repository.save(resourceAssembler.createEntity(resource));

		log.debug("Sending confirmation email to [{}]...", user.getEmail());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("data", messageSource.getMessage("email.registration.body",
				new Object[] { baseProperties.getUrl() + "/api/v1/users?registrationCode=" + user.getRegistrationCode() }, locale));

		try {
			mailSender.send(user.getEmail(), messageSource.getMessage("email.registration.subject", null, locale),
					FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("email_registration.ftl"), model));
		} catch (Exception e) {
			log.error("Sending registration email failed", e);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> resetPassword(UserResource resource, Locale locale) {
		log.debug("Reseting password for email [{}]...", resource.getEmail());

		User user = repository.findByEmail(resource.getEmail());
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String password = passwordService.generate();
		user.setPassword(PasswordService.encodePassword(password));
		user = repository.save(user);

		log.debug("Sending reset password email to [{}]...", user.getEmail());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("data", messageSource.getMessage("email.resetPassword.body", new Object[] { password }, locale));

		try {
			mailSender.send(user.getEmail(), messageSource.getMessage("email.resetPassword.subject", null, locale),
					FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("email_resetPassword.ftl"), model));
		} catch (Exception e) {
			log.error("Sending reset password email failed", e);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Transactional
	public void confirmRegistration(String registrationCode, HttpServletResponse response) {
		log.debug("Confirming registration for code [{}]...", registrationCode);

		User user = repository.findByRegistrationCode(registrationCode);

		if (user != null) {
			if (user.getRegistrationCodeConfirmedDate() == null) {
				user.setStatus(UserStatus.ACTIVE);
				user.setRegistrationCodeConfirmedDate(new DateTime());
				user.setRegistrationType(UserRegistrationType.INTERNAL);
				repository.save(user);

				try {
					response.sendRedirect(baseProperties.getUrl() + "/login.html");
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.warn("Registration already confirmed for code [{}]", registrationCode);
				try {
					response.sendError(HttpServletResponse.SC_CONFLICT, String.format("Registration already confirmed for code [%s]", registrationCode));
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		} else {
			log.warn("Unknown registration code [{}]", registrationCode);
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Unknown registration code [%s]", registrationCode));
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
