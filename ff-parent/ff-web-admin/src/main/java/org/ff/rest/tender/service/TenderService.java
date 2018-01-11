package org.ff.rest.tender.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.ff.jpa.domain.Image;
import org.ff.jpa.domain.Item;
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
import org.ff.jpa.repository.ImageRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserEmailRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.jpa.specification.TenderSpecification;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.debugging.resource.DebuggingResource;
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
	private AlgorithmService algorithmService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private ImageRepository imageRepository;

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
			auditingHandler.markModified(entity);
		}

		repository.save(entity);
		resource = resourceAssembler.toResource(entity, false);

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

		userEmailRepository.deleteByTender(entity);

		repository.delete(entity);
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

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("tenderName", tender.getName());
			String text = mailSender.processTemplateIntoString("email_tender.ftl", model);

			Email email = new Email();
			email.setSubject(resource.getSubject());
			email.setText(text);
			emailRepository.save(email);

			List<MailSenderResource> mailSenderResources = new ArrayList<>();

			Map<String, Set<String>> brms = new HashMap<>();
			Map<String, String> brmSubstitutes = new HashMap<>();

			for (UserGroupResource userGroup : resource.getUserGroups()) {
				if (userGroup.getMetaTag() == UserGroupMetaTag.MATCHING_USERS) {
					List<User> users = algorithmService.findUsers4Tender(tender, new DebuggingResource());
					userGroup.setUsers(userResourceAssembler.toResources(users, true));
				}

				if (userGroup.getUsers() != null) {
					for (UserResource userResource : userGroup.getUsers()) {
						if (userResource.getStatus() != UserStatus.ACTIVE || StringUtils.isBlank(userResource.getEmail())) {
							continue;
						}

						User user = userRepository.findOne(userResource.getId());

						UserEmail userEmail = new UserEmail();
						userEmail.setEmail(email);
						userEmail.setUser(user);
						userEmail.setTender(tender);
						userEmailRepository.save(userEmail);

						mailSenderResources.add(new MailSenderResource(user.getEmail(), StringUtils.isNotBlank(user.getEmail2()) ? user.getEmail2() : null, resource.getSubject(), text));

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
				}
			}

			for (Entry<String, Set<String>> entry : brms.entrySet()) {
				model = new HashMap<String, Object>();
				model.put("originalEmailText", text);
				model.put("users", entry.getValue());
				mailSenderResources.add(new MailSenderResource(entry.getKey(), brmSubstitutes.get(entry.getKey()), resource.getSubject(), mailSender.processTemplateIntoString("email_tender_brm.ftl", model)));
			}

			if (!mailSenderResources.isEmpty()) {
				mailSender.send(mailSenderResources);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			}
		} catch (Exception e) {
			throw new RuntimeException("Sending e-mail failed", e);
		}
	}

	public List<UserResource> findMatchingUsers(Integer id) {
		return userResourceAssembler.toResources(algorithmService.findUsers4Tender(repository.findOne(id), new DebuggingResource()), true);
	}

	@Transactional(readOnly = true)
	public List<TenderResource> exportTenders() {
		return resourceAssembler.toResources(repository.findAll(), false);
	}

	@Transactional(readOnly = true)
	public List<TenderResource> exportTender(Integer id) {
		List<TenderResource> result = new ArrayList<>();
		result.add(resourceAssembler.toResource(repository.findOne(id), false));
		return result;
	}

	@Transactional
	public Integer importTenders(List<TenderResource> tenderResources) {
		int cntImported = 0;

		for (TenderResource tenderResource : tenderResources) {
			log.debug("Importing {}", tenderResource);

			Tender tender = new Tender();
			tender.setStatus(Tender.TenderStatus.INACTIVE);
			tender.setName(tenderResource.getName());
			tender.setText(tenderResource.getText());

			Image image = new Image();
			image.setBase64((tenderResource.getImage() != null) ? tenderResource.getImage().getBase64() : null);
			tender.setImage(imageRepository.save(image));

			tender.setItems(new LinkedHashSet<TenderItem>());

			if (tenderResource.getItems() != null && !tenderResource.getItems().isEmpty()) {
				for (ItemResource itemResource : tenderResource.getItems()) {
					Item item = itemRepository.findByCodeAndEntityType(itemResource.getCode(), ItemEntityType.TENDER);
					if (item != null) {
						TenderItem tenderItem = new TenderItem();
						tenderItem.setTender(tender);
						tenderItem.setItem(item);
						resourceAssembler.setEntityValue(item, itemResource, tenderItem, true);
						tender.getItems().add(tenderItem);
					} else {
						log.warn("Tender item not found for code [{}]", itemResource.getCode());
					}
				}
			}

			repository.save(tender);

			cntImported++;
		}

		return cntImported;
	}

}
