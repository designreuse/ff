package org.ff.rest.tender.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.ff.base.service.BaseService;
import org.ff.common.algorithm.AlgorithmService;
import org.ff.common.mailsender.MailSenderService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridFilterResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.SearchCriteria;
import org.ff.jpa.SearchOperation;
import org.ff.jpa.domain.BusinessRelationshipManager;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.domain.UserEmail;
import org.ff.jpa.repository.EmailRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserEmailRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.jpa.specification.TenderSpecification;
import org.ff.rest.counters.service.CountersService;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.email.resource.SendEmailResource;
import org.ff.rest.image.resource.ImageResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.tender.resource.TenderResourceAssembler;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.ff.rest.usergroup.resource.UserGroupResource;
import org.ff.rest.usergroup.resource.UserGroupResource.UserGroupMetaTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TenderService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TenderRepository repository;

	@Autowired
	private TenderResourceAssembler resourceAssembler;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@Autowired
	private CountersService countersService;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private UserEmailRepository userEmailRepository;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private Configuration configuration;

	@Autowired
	private AlgorithmService algorithmService;

	@Autowired
	private CurrencyService currencyService;

	@Transactional(readOnly = true)
	public TenderResource find(Integer id, Locale locale) {
		log.debug("Finding tender [{}]...", id);

		if (id == 0) {
			TenderResource resource = new TenderResource();
			resource.setImage(new ImageResource());
			resource.setItems(itemResourceAssembler.toResources(itemRepository.findByEntityTypeAndStatusOrderByPosition(ItemEntityType.TENDER, ItemStatus.ACTIVE), false));

			for (ItemResource itemResource : resource.getItems()) {
				if (itemResource.getType() == ItemType.CURRENCY && itemResource.getCurrency() == null) {
					itemResource.setCurrency(currencyService.findAll().get(0));
				}
			}

			return resource;
		}

		Tender entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.tender", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

	@Transactional(readOnly = true)
	public List<TenderResource> findAll() {
		List<TenderResource> result = new ArrayList<>();

		for (Tender tender : repository.findAll()) {
			TenderResource resource = new TenderResource();
			resource.setId(tender.getId());
			resource.setName(tender.getName());
			resource.setText(tender.getText());
			if (tender.getImage() != null && StringUtils.isNotBlank(tender.getImage().getBase64())) {
				resource.setImageId(tender.getImage().getId());
			}
			resource.setLastModifiedDate(tender.getLastModifiedDate().toDate());

			resource.setItems(new ArrayList<ItemResource>());
			for (TenderItem tenderItem : tender.getItems()) {
				if (Boolean.TRUE == tenderItem.getItem().getWidgetItem()) {
					ItemResource itemResource = itemResourceAssembler.toResource(tenderItem.getItem(), true);
					itemResource.setValue(tenderItem.getValue());
					itemResource.setValueMapped(tenderItem.getValue());

					if (itemResource.getType() == ItemType.CURRENCY && itemResource.getCurrency() == null) {
						itemResource.setCurrency(currencyService.findAll().get(0));
					}

					resource.getItems().add(itemResource);
				}
			}

			result.add(resource);
		}

		return result;
	}

	@Transactional
	public TenderResource save(TenderResource resource) {
		log.debug("Saving tender [{}]...", resource);

		Tender entity = null;
		if (resource.getId() == null) {
			entity = resourceAssembler.createEntity(resource);
		} else {
			entity = repository.findOne(resource.getId());
			entity = resourceAssembler.updateEntity(entity, resource);
		}

		repository.save(entity);
		resource = resourceAssembler.toResource(entity, false);

		countersService.sendEvent();

		return resource;
	}

	@Transactional
	public TenderResource activate(Integer id, Locale locale) {
		log.debug("Activating tender [{}]...", id);

		Tender entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.tender", null, locale), id }, locale));
		}

		entity.setStatus(TenderStatus.ACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public TenderResource deactivate(Integer id, Locale locale) {
		log.debug("Deactivating tender [{}]...", id);

		Tender entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.tender", null, locale), id }, locale));
		}

		entity.setStatus(TenderStatus.INACTIVE);
		repository.save(entity);

		return resourceAssembler.toResource(entity, true);
	}

	@Transactional
	public void delete(Integer id, Locale locale) {
		log.debug("Deleting tender [{}]...", id);

		Tender entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.tender", null, locale), id }, locale));
		}

		repository.delete(entity);

		countersService.sendEvent();
	}

	@Transactional(readOnly = true)
	public PageableResource<TenderResource> getPage(UiGridResource resource) {
		Page<Tender> page = null;

		List<Specification<Tender>> specifications = new ArrayList<>();
		if (resource.getFilter() != null && !resource.getFilter().isEmpty()) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				TenderSpecification specification = createSpecification(uiGridFilterResource);
				if (specification != null) {
					specifications.add(specification);
				}
			}
		}

		if (!specifications.isEmpty()) {
			// filtering
			Specification<Tender> specification = specifications.get(0);
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

	private TenderSpecification createSpecification(UiGridFilterResource resource) {
		if (resource.getName().equalsIgnoreCase("id")) {
			return new TenderSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, resource.getTerm()));
		} else if (resource.getName().equalsIgnoreCase("status")) {
			if (EnumUtils.isValidEnum(TenderStatus.class, resource.getTerm().toUpperCase())) {
				return new TenderSpecification(new SearchCriteria(resource.getName(), SearchOperation.EQUALITY, TenderStatus.valueOf(resource.getTerm().toUpperCase())));
			} else {
				return null;
			}
		} else if (resource.getName().equalsIgnoreCase("creationDate") || resource.getName().equalsIgnoreCase("lastModifiedDate")) {
			return new TenderSpecification(new SearchCriteria(resource.getName(), SearchOperation.BETWEEN, getDateRange(resource.getTerm())));
		} else {
			return new TenderSpecification(new SearchCriteria(resource.getName(), SearchOperation.CONTAINS, resource.getTerm()));
		}
	}

	@Transactional
	public ResponseEntity<?> sendEmail(SendEmailResource resource) {
		try {
			Tender tender = repository.findOne(resource.getTenderId());

			Template template = configuration.getTemplate("email_tender.ftl");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("tenderName", tender.getName());
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

			Email email = new Email();
			email.setSubject(resource.getSubject());
			email.setText(text);

			Set<String> to = new HashSet<>();
			Map<String, Set<String>> businessRelationshipManagers = new HashMap<>();
			for (UserGroupResource userGroup : resource.getUserGroups()) {
				if (userGroup.getMetaTag() == UserGroupMetaTag.MATCHING_USERS) {
					List<User> users = algorithmService.findUsers4Tender(tender);
					userGroup.setUsers(userResourceAssembler.toResources(users, true));
				}

				if (userGroup.getUsers() != null) {
					for (UserResource userResource : userGroup.getUsers()) {
						User user = userRepository.findOne(userResource.getId());
						if (user.getStatus() != UserStatus.ACTIVE) {
							continue;
						}

						to.add(user.getEmail());
						if (StringUtils.isNotBlank(user.getEmail2())) {
							to.add(user.getEmail2());
						}

						BusinessRelationshipManager brm = user.getBusinessRelationshipManager();
						if (brm != null && StringUtils.isNotBlank(brm.getEmail())) {
							if (!businessRelationshipManagers.containsKey(brm.getEmail())) {
								businessRelationshipManagers.put(brm.getEmail(), new HashSet<String>());
							}
							businessRelationshipManagers.get(brm.getEmail()).add(user.getEmail());
						}

						UserEmail userEmail = new UserEmail();
						userEmail.setEmail(email);
						userEmail.setUser(user);
						userEmail.setTender(tender);
						userEmailRepository.save(userEmail);
					}
				}
			}

			if (!to.isEmpty()) {
				mailSender.send(to.toArray(new String[to.size()]), resource.getSubject(), text);
				emailRepository.save(email);

				// send e-mail to business relationship manager(s)
				for (Entry<String, Set<String>> entry : businessRelationshipManagers.entrySet()) {
					sendEmail2BusinessRelationshipManager(entry.getKey(), entry.getValue(), resource.getSubject(), text);
				}

				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			}
		} catch (Exception e) {
			throw new RuntimeException("Sending e-mail failed", e);
		}
	}

	private void sendEmail2BusinessRelationshipManager(String to, Set<String> users, String originalEmailSubject, String originalEmailText) {
		try {
			Template template = configuration.getTemplate("email_tender_brm.ftl");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("originalEmailText", originalEmailText);
			model.put("users", users);
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

			mailSender.send(to, "FYI: " + originalEmailSubject, text);
		} catch (Exception e) {
			log.error(String.format("Sending e-mail to business relationship manager [%s] failed", to), e);
		}
	}

	public List<UserResource> findMatchingUsers(Integer id) {
		return userResourceAssembler.toResources(algorithmService.findUsers4Tender(repository.findOne(id)), true);
	}

}
