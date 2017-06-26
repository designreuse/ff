package org.ff.rest.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.mailsender.MailSenderService;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.repository.CompanyRepository;
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
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GfiSyncService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private Configuration configuration;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private ZabaApiService zabaApiService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ZabaUpdateService zabaUpdateService;

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

		List<String> to = new ArrayList<>();
		for (User user : users) {
			try {
				Company company = user.getCompany();
				if (company != null) {
					log.debug("Syncing data for company [{}]", company.getName());

					// get company data from external source (e.g. from ZaBa) via ReST API
					ZabaCompanyResource zabaCompanyResource = zabaApiService.getCompanyData(company.getCompanyNumber() + "-" + company.getBranchOfficeNumber());

					// update company data
					zabaUpdateService.updateCompanyData(company, zabaCompanyResource);

					// update last GFI sync date
					company.setLastGfiSync(new DateTime());
					companyRepository.save(company);

					result.getUpdateOK().add(userResourceAssembler.toResource(user, true));

					if (StringUtils.isNotBlank(user.getEmail())) {
						to.add(user.getEmail());
					}
					if (StringUtils.isNotBlank(user.getEmail2())) {
						to.add(user.getEmail2());
					}
				}
			} catch (Exception e) {
				result.getUpdateNOK().add(userResourceAssembler.toResource(user, true));
				log.error("GFI sync failed for user with ID: " + user.getId(), e);
			}
		}

		// send e-mail
		try {
			Template template = configuration.getTemplate("email_gfi_sync.ftl");
			Map<String, Object> model = new HashMap<String, Object>();
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

			mailSender.send(to.toArray(new String[to.size()]), baseProperties.getGfiSyncEmailSubject(), text);
		} catch (Exception e) {
			log.error("Sending GFI sync e-mail failed", e);
		}

		log.debug("GFI sync finished in {} ms", System.currentTimeMillis() - start);

		return result;
	}

}
