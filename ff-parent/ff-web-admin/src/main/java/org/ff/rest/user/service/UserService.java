package org.ff.rest.user.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.ff.base.service.BaseService;
import org.ff.common.algorithm.AlgorithmService;
import org.ff.common.mailsender.MailSenderResource;
import org.ff.common.mailsender.MailSenderService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.Project;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.domain.UserEmail;
import org.ff.jpa.domain.UserGroup;
import org.ff.jpa.repository.BusinessRelationshipManagerRepository;
import org.ff.jpa.repository.EmailRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.UserEmailRepository;
import org.ff.jpa.repository.UserGroupRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.jpa.specification.UserSpecification;
import org.ff.rest.debugging.resource.DebuggingResource;
import org.ff.rest.email.resource.SendEmailResource;
import org.ff.rest.project.resource.ProjectResource;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.tender.resource.TenderResourceAssembler;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private AlgorithmService algorithmService;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private Collator collator;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserEmailRepository userEmailRepository;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private BusinessRelationshipManagerRepository businessRelationshipManagerRepository;

	@Autowired
	private UserGroupRepository userGroupRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Transactional(readOnly = true)
	public List<UserResource> findAll() {
		log.debug("Finding users...");

		List<UserResource> result = resourceAssembler.toResources(repository.findAll(), true);
		Collections.sort(result, new Comparator<UserResource>() {
			@Override
			public int compare(UserResource o1, UserResource o2) {
				return collator.compare(o1.getLastName(), o2.getLastName());
			}
		});

		return result;
	}

	@Transactional(readOnly = true)
	public UserResource find(Integer id, Locale locale) {
		log.debug("Finding user [{}]...", id);

		User entity = repository.findOne(id);
		if (entity == null || Boolean.TRUE == entity.getDemoUser()) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.user", null, locale), id }, locale));
		}

		UserResource resource = resourceAssembler.toResource(entity, false);

		List<Tender> tenders = algorithmService.findTenders4User(entity, entity.getCompany(),
				projectRepository.findByCompany(entity.getCompany()), new DebuggingResource());

		if (tenders != null && !tenders.isEmpty()) {
			if (entity.getCompany() != null) {
				List<Project> projects = projectRepository.findByCompany(entity.getCompany());
				if (projects != null) {
					for (Tender tender : tenders) {
						algorithmService.processTender4Projects(tender, projects);
					}
				}
			}

			resource.getCompany().setTenders(tenderResourceAssembler.toResources(tenders, true));
		}

		if (resource.getCompany().getTenders() != null) {
			for (TenderResource tenderResource : resource.getCompany().getTenders()) {
				if (tenderResource.getProjects() != null) {
					Collections.sort(tenderResource.getProjects(), new Comparator<ProjectResource>() {
						@Override
						public int compare(ProjectResource o1, ProjectResource o2) {
							return collator.compare(o1.getName(), o2.getName());
						}
					});
				}
			}
		}

		for (ProjectResource projectResource : resource.getProjects()) {
			projectResource.setMatchingTenders(getTenders4Project(projectResource, resource.getCompany().getTenders()));
		}

		return resource;
	}

	private List<TenderResource> getTenders4Project(ProjectResource project, List<TenderResource> tenders) {
		List<TenderResource> result = new ArrayList<>();
		if (tenders != null) {
			for (TenderResource tenderResource : tenders) {
				for (ProjectResource projectResource : tenderResource.getProjects()) {
					if (projectResource.getId().equals(project.getId())) {
						if (!result.contains(tenderResource)) {
							result.add(tenderResource);
						}
					}
				}
			}
		}

		Collections.sort(result, new Comparator<TenderResource>() {
			@Override
			public int compare(TenderResource o1, TenderResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

	@Transactional
	public UserResource save(UserResource resource) {
		log.debug("Saving user [{}]...", resource);

		User entity = null;
		if (resource.getId() == null) {
			entity = resourceAssembler.createEntity(resource);
		} else {
			entity = repository.findOne(resource.getId());
			entity = resourceAssembler.updateEntity(entity, resource);
		}

		repository.save(entity);
		resource = resourceAssembler.toResource(entity, false);

		return resource;
	}

	@Transactional
	public void setBusinessRelationshipManager(Integer userId, UserResource resource) {
		User entity = repository.findOne(userId);
		entity.setBusinessRelationshipManager((resource.getBusinessRelationshipManager() != null) ? businessRelationshipManagerRepository.findOne(resource.getBusinessRelationshipManager().getId()) : null);
		entity.setBusinessRelationshipManagerSubstitute((resource.getBusinessRelationshipManagerSubstitute() != null) ? businessRelationshipManagerRepository.findOne(resource.getBusinessRelationshipManagerSubstitute().getId()) : null);
		repository.save(entity);
	}

	@Transactional
	public UserResource activate(Integer id, Locale locale) {
		log.debug("Activating user [{}]...", id);

		User entity = repository.findOne(id);
		if (entity == null || Boolean.TRUE == entity.getDemoUser()) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.user", null, locale), id }, locale));
		}

		entity.setStatus(UserStatus.ACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public UserResource deactivate(Integer id, Locale locale) {
		log.debug("Deactivating user [{}]...", id);

		User entity = repository.findOne(id);
		if (entity == null || Boolean.TRUE == entity.getDemoUser()) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.user", null, locale), id }, locale));
		}

		entity.setStatus(UserStatus.INACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting user [{}]...", id);

		User entity = repository.findOne(id);
		if (entity == null || Boolean.TRUE == entity.getDemoUser()) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.user", null, locale), id }, locale));
		}

		// delete projects
		Long cntDeletedProjects = projectRepository.deleteByCompany(entity.getCompany());
		if (cntDeletedProjects > 0) {
			log.debug("{} user/company projects deleted", cntDeletedProjects);
		}

		// delete e-mails
		Long cntDeletedEmails = userEmailRepository.deleteByUser(entity);
		if (cntDeletedEmails > 0) {
			log.debug("{} user e-mails deleted", cntDeletedEmails);
		}

		// remove from user groups
		List<UserGroup> userGroups = userGroupRepository.findByUsersIn(entity);
		for (UserGroup userGroup : userGroups) {
			log.debug("Removing user [{}] from user group [{}]", entity.getId(), userGroup.getId());
			userGroup.getUsers().remove(entity);
			userGroupRepository.save(userGroup);
		}

		// delete
		repository.delete(entity);
		log.debug("User [{}] deleted", entity.getId());
	}

	@Transactional(readOnly = true)
	public PageableResource<UserResource> getPage(UiGridResource resource) {
		Page<User> page = null;

		List<Specification<User>> specifications = new ArrayList<>();

		specifications.add(new UserSpecification(new SearchCriteria("demoUser", SearchOperation.EQUALITY, Boolean.FALSE)));

		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				UserSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<User> specification = specifications.get(0);
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

	private UserSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new UserSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("status")) {
			if (EnumUtils.isValidEnum(UserStatus.class, resource.getTerm().toUpperCase())) {
				return new UserSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, UserStatus.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("registrationType")) {
			if (EnumUtils.isValidEnum(UserRegistrationType.class, resource.getTerm().toUpperCase())) {
				return new UserSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, UserRegistrationType.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new UserSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new UserSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

	/**
	 * Method sends e-mail to a single user.
	 * @param resource
	 */
	@Transactional
	public void sendEmail(SendEmailResource resource) {
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("text", textToHTML(resource.getText()));
			String text = mailSender.processTemplateIntoString("email_user.ftl", model);

			User user = userRepository.findOne(resource.getUsers().get(0).getId());

			Email email = new Email();
			email.setSubject(resource.getSubject());
			email.setText(text);
			emailRepository.save(email);

			UserEmail userEmail = new UserEmail();
			userEmail.setEmail(email);
			userEmail.setUser(user);
			userEmailRepository.save(userEmail);

			// send e-mail
			mailSender.send(new MailSenderResource(user.getEmail(), StringUtils.isNotBlank(user.getEmail2()) ? user.getEmail2() : null, resource.getSubject(), text));

			// send e-mail to BRM
			send2Brm(user, resource.getSubject(), text);
		} catch (Exception e) {
			throw new RuntimeException("Sending e-mail failed", e);
		}
	}

	private void send2Brm(User user, String subject, String text) {
		try {
			if (user.getBusinessRelationshipManager() != null && StringUtils.isNotBlank(user.getBusinessRelationshipManager().getEmail())) {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("originalEmailText", text);
				model.put("user", user.getEmail());
				mailSender.send(new MailSenderResource(user.getBusinessRelationshipManager().getEmail(), (user.getBusinessRelationshipManagerSubstitute() != null && StringUtils.isNotBlank((user.getBusinessRelationshipManagerSubstitute().getEmail()))) ? user.getBusinessRelationshipManagerSubstitute().getEmail() : null, subject, mailSender.processTemplateIntoString("email_user_brm.ftl", model)));
			}
		} catch (Exception e) {
			log.error(String.format("Sending e-mail to business relationship manager [%s] failed", user.getBusinessRelationshipManager().getEmail()), e);
		}
	}

	public static String textToHTML(String text) {
		if (text == null) {
			return null;
		}
		int length = text.length();
		boolean prevSlashR = false;
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char ch = text.charAt(i);
			switch(ch) {
				case '\r':
					if (prevSlashR) {
						out.append("<br>");
					}
					prevSlashR = true;
					break;
				case '\n':
					prevSlashR = false;
					out.append("<br>");
					break;
				case '"':
					if (prevSlashR) {
						out.append("<br>");
						prevSlashR = false;
					}
					out.append("&quot;");
					break;
				case '<':
					if (prevSlashR) {
						out.append("<br>");
						prevSlashR = false;
					}
					out.append("&lt;");
					break;
				case '>':
					if (prevSlashR) {
						out.append("<br>");
						prevSlashR = false;
					}
					out.append("&gt;");
					break;
				case '&':
					if (prevSlashR) {
						out.append("<br>");
						prevSlashR = false;
					}
					out.append("&amp;");
					break;
				default:
					if (prevSlashR) {
						out.append("<br>");
						prevSlashR = false;
					}
					out.append(ch);
					break;
			}
		}
		return out.toString();
	}

}
