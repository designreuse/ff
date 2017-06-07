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
import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.company.service.CompanyService;
import org.ff.rest.project.resource.ProjectResource;
import org.ff.rest.project.service.ProjectService;
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

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private ProjectService projectService;

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

	@Transactional
	public ResponseEntity<?> registerDemo(UserResource userResource, AppUserDetails principal, Locale locale) {
		log.debug("Registering demo user [{}]...", userResource);

		try {
			// validate user (e-mail)
			User user = repository.findByEmail(userResource.getEmail());
			if (user != null) {
				// error code 200 indicates that user with given e-mail already exists
				return new ResponseEntity<>(100, HttpStatus.CONFLICT);
			}

			// validate company (code/OIB)
			Company company = companyRepository.findByCode(userResource.getCompany().getCode());
			if (company != null) {
				// error code 200 indicates that company with given code (OIB) already exists
				return new ResponseEntity<>(200, HttpStatus.CONFLICT);
			}

			// create user (and company)
			userResource.setRegistrationType(UserRegistrationType.INTERNAL);
			userResource.setStatus(UserStatus.WAITING_CONFIRMATION);
			user = repository.save(resourceAssembler.createEntity(userResource));

			// update company
			CompanyResource companyResource = companyResourceAssembler.toResource(user.getCompany(), true);
			companyResource.setItems(userResource.getCompany().getItems());
			companyService.save(principal, companyResource);

			// create projects
			if (userResource.getProjects() != null) {
				for (ProjectResource projectResource : userResource.getProjects()) {
					// nullify id
					projectResource.setId(null);
					projectResource.setCompany(companyResource);
					projectService.save(principal, projectResource);
				}
			}

			// send confirmation e-mail
			log.debug("Sending confirmation email to [{}]...", user.getEmail());

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("data", messageSource.getMessage("email.registration.body",
					new Object[] { baseProperties.getUrl() + "/api/v1/users?registrationCode=" + user.getRegistrationCode() }, locale));

			mailSender.send(user.getEmail(), messageSource.getMessage("email.registration.subject", null, locale),
					FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("email_registration.ftl"), model));

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Registering demo user failed", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
