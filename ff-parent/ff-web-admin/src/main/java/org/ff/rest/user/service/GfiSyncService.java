package org.ff.rest.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.mailsender.MailSenderResource;
import org.ff.common.mailsender.MailSenderService;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.UserEmail;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.EmailRepository;
import org.ff.jpa.repository.UserEmailRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.user.resource.GfiSyncReportResource;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.ff.zaba.resource.ZabaCompanyResource;
import org.ff.zaba.service.ZabaApiService;
import org.ff.zaba.service.ZabaUpdateService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GfiSyncService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private UserEmailRepository userEmailRepository;

	@Autowired
	private ZabaApiService zabaApiService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ZabaUpdateService zabaUpdateService;

	@Transactional
	public GfiSyncReportResource gfiSync(UserResource resource, Locale locale) {
		GfiSyncReportResource result = new GfiSyncReportResource();

		long start = System.currentTimeMillis();
		List<User> users = new ArrayList<>();

		if (resource == null) {
			log.info("Initiating GFI sync for all users with registration type EXTERNAL...");
			for (User user : userRepository.findByRegistrationType(UserRegistrationType.EXTERNAL)) {
				users.add(user);
			}
		} else {
			log.info("Initiating GFI sync for {}", resource);
			User user = userRepository.findOne(resource.getId());
			if (user == null) {
				throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
						new Object[] { messageSource.getMessage("resource.user", null, locale), resource.getId() }, locale));
			}
			users.add(user);
		}

		// e-mail template
		String text = mailSender.processTemplateIntoString("email_gfi_sync.ftl", new HashMap<String, Object>());

		// save e-mail record in database
		Email email = new Email();
		email.setSubject(baseProperties.getGfiSyncEmailSubject());
		email.setText(text);
		emailRepository.save(email);

		Map<String, Set<String>> brms = new HashMap<>();
		Map<String, String> brmSubstitutes = new HashMap<>();

		for (User user : users) {
			try {
				Company company = user.getCompany();
				if (company != null) {
					log.debug("Syncing data for company [{}]", company.getName());

					// get company data from external source (e.g. from ZaBa) via ReST API
					ZabaCompanyResource zabaCompanyResource = zabaApiService.getCompanyData(company.getCompanyNumber() + "-" + company.getBranchOfficeNumber());

					// update company data
					zabaUpdateService.updateCompanyData(company, zabaCompanyResource, true);

					// update last GFI sync date
					company.setLastGfiSync(new DateTime());
					companyRepository.save(company);

					result.getUpdateOK().add(userResourceAssembler.toResource(user, true));

					UserEmail userEmail = new UserEmail();
					userEmail.setEmail(email);
					userEmail.setUser(user);
					userEmailRepository.save(userEmail);

					mailSender.send(new MailSenderResource(user.getEmail(), StringUtils.isNotBlank(user.getEmail2()) ? user.getEmail2() : null, baseProperties.getGfiSyncEmailSubject(), text));

					if (user.getBusinessRelationshipManager() != null && StringUtils.isNotBlank(user.getBusinessRelationshipManager().getEmail())) {
						if (!brms.containsKey(user.getBusinessRelationshipManager().getEmail())) {
							brms.put(user.getBusinessRelationshipManager().getEmail(), new HashSet<String>());
						}
						brms.get(user.getBusinessRelationshipManager().getEmail()).add(user.getEmail());

						if (user.getBusinessRelationshipManagerSubstitute() != null && StringUtils.isNotBlank((user.getBusinessRelationshipManagerSubstitute().getEmail()))) {
							brmSubstitutes.put(user.getBusinessRelationshipManager().getEmail(), user.getBusinessRelationshipManagerSubstitute().getEmail());
						}
					}
				}
			} catch (Exception e) {
				result.getUpdateNOK().add(userResourceAssembler.toResource(user, true));
				log.error("GFI sync failed for user with ID: " + user.getId(), e);
			}
		}

		// send e-mails to BRMs
		List<MailSenderResource> mailSenderResources = new ArrayList<>();
		for (Entry<String, Set<String>> entry : brms.entrySet()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("originalEmailText", text);
			model.put("users", entry.getValue());
			mailSenderResources.add(new MailSenderResource(entry.getKey(), brmSubstitutes.get(entry.getKey()), baseProperties.getGfiSyncEmailSubject(), mailSender.processTemplateIntoString("email_tender_brm.ftl", model)));
		}
		mailSender.send(mailSenderResources);

		log.debug("GFI sync finished in {} ms", System.currentTimeMillis() - start);

		return result;
	}

}
