package org.ff.rest.gfisync.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.base.service.BaseService;
import org.ff.common.mailsender.MailSenderResource;
import org.ff.common.mailsender.MailSenderService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.GfiSync;
import org.ff.jpa.domain.GfiSyncError;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.UserEmail;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.EmailRepository;
import org.ff.jpa.repository.GfiSyncRepository;
import org.ff.jpa.repository.UserEmailRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.jpa.specification.GfiSyncSpecification;
import org.ff.rest.gfisync.resource.GfiSyncResource;
import org.ff.rest.gfisync.resource.GfiSyncResourceAssembler;
import org.ff.rest.user.resource.GfiSyncReportResource;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.ff.zaba.resource.ZabaCompanyResource;
import org.ff.zaba.service.ZabaApiService;
import org.ff.zaba.service.ZabaUpdateService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GfiSyncService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private GfiSyncRepository repository;

	@Autowired
	private GfiSyncResourceAssembler resourceAssembler;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private MailSenderService mailSender;

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
	private GfiSyncRepository gfiSyncRepository;

	@Autowired
	private ZabaUpdateService zabaUpdateService;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional(readOnly = true)
	public GfiSyncResource find(Integer id, Locale locale) {
		log.debug("Finding GfiSync [{}]...", id);

		GfiSync entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.gfiSync", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting GfiSync [{}]...", id);

		GfiSync entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.gfiSync", null, locale), id }, locale));
		}

		repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public PageableResource<GfiSyncResource> getPage(UiGridResource resource) {
		Page<GfiSync> page = null;

		List<Specification<GfiSync>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				GfiSyncSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<GfiSync> specification = specifications.get(0);
			if (specifications.size() > 1) {
				for (int i=1; i<specifications.size(); i++) {
					specification = Specifications.where(specification).and(specifications.get(i));
				}
			}
			page = repository.findAll(specification, createPageable(resource));
		} else {
			// no filtering
			page = repository.findAll(createPageable(resource));
		}

		return new PageableResource<>(page.getTotalElements(), resourceAssembler.toResources(page.getContent(), true));
	}

	private GfiSyncSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new GfiSyncSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new GfiSyncSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new GfiSyncSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public GfiSyncReportResource gfiSync(UserResource resource, Locale locale) {
		GfiSync gfiSync = new GfiSync();
		gfiSync.setErrors(new HashSet<GfiSyncError>());

		GfiSyncReportResource result = new GfiSyncReportResource();

		long start = System.currentTimeMillis();
		gfiSync.setStartTime(new DateTime(start));

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

		ZabaCompanyResource zabaCompanyResource = null;

		for (User user : users) {
			zabaCompanyResource = null;

			try {
				Company company = user.getCompany();
				if (company != null) {
					log.debug("Syncing data for company [{}]", company.getName());

					// get company data from external source (e.g. from ZaBa) via ReST API
					zabaCompanyResource = zabaApiService.getCompanyData(company.getCompanyNumber() + "-" + company.getBranchOfficeNumber());

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

					if (StringUtils.isNotBlank(user.getEmail())) {
						try {
							mailSender.send(new MailSenderResource(user.getEmail(), StringUtils.isNotBlank(user.getEmail2()) ? user.getEmail2() : null, baseProperties.getGfiSyncEmailSubject(), text));
						} catch (Exception e) {
							log.warn("Sending GFI sync e-mail to user failed", e);
						}
					} else {
						log.debug("GFI sync e-mail not sent to user with ID [{}] as e-mail not set", user.getId());
					}

					if (user.getBusinessRelationshipManager() != null && StringUtils.isNotBlank(user.getBusinessRelationshipManager().getEmail())) {
						if (!brms.containsKey(user.getBusinessRelationshipManager().getEmail())) {
							brms.put(user.getBusinessRelationshipManager().getEmail(), new HashSet<String>());
						}
						if (StringUtils.isNotBlank(user.getEmail())) {
							brms.get(user.getBusinessRelationshipManager().getEmail()).add(user.getEmail());
						}

						if (user.getBusinessRelationshipManagerSubstitute() != null && StringUtils.isNotBlank((user.getBusinessRelationshipManagerSubstitute().getEmail()))) {
							brmSubstitutes.put(user.getBusinessRelationshipManager().getEmail(), user.getBusinessRelationshipManagerSubstitute().getEmail());
						}
					}
				}
			} catch (Exception e) {
				result.getUpdateNOK().add(userResourceAssembler.toResource(user, true));

				GfiSyncError gfiSyncError = new GfiSyncError();
				gfiSyncError.setGfiSync(gfiSync);
				gfiSyncError.setUser(user);
				gfiSyncError.setError(ExceptionUtils.getStackTrace(e));

				if (zabaCompanyResource != null) {
					try {
						gfiSyncError.setCompanyData(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(zabaCompanyResource));
					} catch (JsonProcessingException exc) {
						log.warn("Parsing of ZabaCompanyResource failed", e);
					}
				}
				gfiSync.getErrors().add(gfiSyncError);

				log.error("GFI sync failed for user with ID: " + user.getId(), e);
			}
		}

		// send e-mails to BRMs
		try {
			log.debug("Sending e-mail to BRMs and their substitutes...");
			List<MailSenderResource> mailSenderResources = new ArrayList<>();
			for (Entry<String, Set<String>> entry : brms.entrySet()) {
				if (StringUtils.isBlank(entry.getKey())) {
					continue;
				}
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("originalEmailText", text);
				model.put("users", entry.getValue());
				mailSenderResources.add(new MailSenderResource(entry.getKey(), brmSubstitutes.get(entry.getKey()), baseProperties.getGfiSyncEmailSubject(), mailSender.processTemplateIntoString("email_tender_brm.ftl", model)));
			}
			mailSender.send(mailSenderResources);
		} catch (Exception e) {
			log.warn("Sending GFI sync e-mails to BRMs failed", e);
		}

		// send GFI sync report e-mail
		sendGfiSyncReportEmail(start, result);

		gfiSync.setCntTotal(result.getUpdateOK().size() + result.getUpdateNOK().size());
		gfiSync.setCntOk(result.getUpdateOK().size());
		gfiSync.setCntNok(result.getUpdateNOK().size());
		gfiSync.setEndTime(new DateTime(System.currentTimeMillis()));
		gfiSyncRepository.save(gfiSync);

		log.debug("GFI sync finished in {} ms", System.currentTimeMillis() - start);

		return result;
	}

	private void sendGfiSyncReportEmail(long start, GfiSyncReportResource result) {
		try {
			log.debug("Sending GFI sync report e-mail...");

			DateFormat dateTimeFormat = new SimpleDateFormat(baseProperties.getDateTimeFormat());
			DateFormat dateFormat = new SimpleDateFormat(baseProperties.getDateFormat());
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("startDate", dateFormat.format(new Date(start)));
			model.put("startTime", timeFormat.format(new Date(start)));
			model.put("cntUsers", Integer.toString(result.getUpdateOK().size() + result.getUpdateNOK().size()));
			model.put("cntUsersOk", Integer.toString(result.getUpdateOK().size()));
			model.put("cntUsersNok", Integer.toString(result.getUpdateNOK().size()));

			Set<String> users = new HashSet<>();
			for (UserResource userResource : result.getUpdateNOK()) {
				StringBuffer sb = new StringBuffer();
				sb.append(userResource.getId()).append(",");
				sb.append(userResource.getFirstName()).append(" ").append(userResource.getLastName()).append(",");
				sb.append(StringUtils.isNotBlank(userResource.getCompany().getName()) ? userResource.getCompany().getName() : "").append(",");
				sb.append(StringUtils.isNotBlank(userResource.getCompany().getCode()) ? userResource.getCompany().getCode() : "").append(",");
				sb.append(StringUtils.isNotBlank(userResource.getEmail()) ? userResource.getEmail() : "").append(",");
				sb.append(StringUtils.isNotBlank(userResource.getEmail2()) ? userResource.getEmail2() : "").append(",");
				if (userResource.getBusinessRelationshipManager() != null) {
					sb.append(userResource.getBusinessRelationshipManager().getFirstName()).append(" ").append(userResource.getBusinessRelationshipManager().getLastName()).append(",");
				} else {
					sb.append("").append(",");
				}
				if (userResource.getBusinessRelationshipManagerSubstitute() != null) {
					sb.append(userResource.getBusinessRelationshipManagerSubstitute().getFirstName()).append(" ").append(userResource.getBusinessRelationshipManagerSubstitute().getLastName()).append(",");
				} else {
					sb.append("").append(",");
				}
				if (userResource.getLastLoginDate() != null) {
					sb.append(dateTimeFormat.format(userResource.getLastLoginDate()));
				} else {
					sb.append("");
				}
				users.add(sb.toString());
			}
			model.put("users", users);

			for (String to : StringUtils.split(baseProperties.getGfiSyncReportEmailTo(), "|")) {
				mailSender.send(new MailSenderResource(to, null, baseProperties.getGfiSyncReportEmailSubject(), mailSender.processTemplateIntoString("email_gfi_sync_report.ftl", model)));
			}

			log.debug("GFI sync report e-mail successfully sent");
		} catch (Exception e) {
			log.warn("Sending GFI sync report e-mail failed", e);
		}
	}

}
