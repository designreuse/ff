package org.ff.rest.tender.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ff.common.algorithm.AlgorithmService;
import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.Project;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.company.resource.CompanyResourceAssembler;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.debugging.resource.DebuggingResource;
import org.ff.rest.image.resource.ImageResourceAssembler;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.project.resource.ProjectResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.ff.rest.tender.resource.DemoResource;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.tender.resource.TenderResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenderService {

	@Autowired
	private Collator collator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlgorithmService algorithmService;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private ImpressionRepository impressionRepository;

	@Autowired
	private ImageResourceAssembler imageResourceAssembler;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private ProjectResourceAssembler projectResourceAssembler;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private ProjectRepository companyInvestmentRepository;

	@Transactional(readOnly = true)
	public List<TenderResource> findAll(AppUserDetails principal) {
		List<TenderResource> result = new ArrayList<>();

		User user = userRepository.findOne(principal.getUser().getId());

		for (Tender tender : algorithmService.findTenders4User(user, new DebuggingResource())) {
			TenderResource resource = new TenderResource();
			resource.setId(tender.getId());
			resource.setStatus(tender.getStatus());
			resource.setName(tender.getName());
			resource.setText(tender.getText());
			if (tender.getImage() != null && StringUtils.isNotBlank(tender.getImage().getBase64())) {
				resource.setImageId(tender.getImage().getId());
			}
			resource.setCreationDate(tender.getCreationDate().toDate());
			resource.setCreatedBy(tender.getCreatedBy());
			resource.setLastModifiedDate(tender.getLastModifiedDate().toDate());
			resource.setLastModifiedBy(tender.getLastModifiedBy());

			resource.setProjects(projectResourceAssembler.toResources(tender.getProjects(), true));

			Collections.sort(resource.getProjects(), new Comparator<ProjectResource>() {
				@Override
				public int compare(ProjectResource o1, ProjectResource o2) {
					return collator.compare(o1.getName(), o2.getName());
				}
			});

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

	@Transactional(readOnly = true)
	public List<TenderResource> findAllDemo(AppUserDetails principal, DemoResource demoResource) {
		List<TenderResource> result = new ArrayList<>();

		if (demoResource.getCompany() == null) {
			return result;
		}

		User user = userRepository.findOne(principal.getUser().getId());

		CompanyResource companyResource = demoResource.getCompany();

		Company company = user.getCompany();

		for (ItemResource itemResource : companyResource.getItems()) {
			CompanyItem companyItem = new CompanyItem();
			companyItem.setCompany(company);
			companyItem.setItem(itemRepository.findOne(itemResource.getId()));
			// set company item value
			companyResourceAssembler.setEntityValue(itemResource, companyItem);
			company.getItems().add(companyItem);
		}

		for (Tender tender : algorithmService.findTenders4User(user, new DebuggingResource())) {
			TenderResource tenderResource = new TenderResource();
			tenderResource.setId(tender.getId());
			tenderResource.setStatus(tender.getStatus());
			tenderResource.setName(tender.getName());
			tenderResource.setText(tender.getText());
			if (tender.getImage() != null) {
				tenderResource.setImage(imageResourceAssembler.toResource(tender.getImage(), false));
			}
			tenderResource.setCreationDate(tender.getCreationDate().toDate());
			tenderResource.setCreatedBy(tender.getCreatedBy());
			tenderResource.setLastModifiedDate(tender.getLastModifiedDate().toDate());
			tenderResource.setLastModifiedBy(tender.getLastModifiedBy());

			tenderResource.setItems(new ArrayList<ItemResource>());
			for (TenderItem tenderItem : tender.getItems()) {
				if (Boolean.TRUE == tenderItem.getItem().getWidgetItem()) {
					ItemResource itemResource = itemResourceAssembler.toResource(tenderItem.getItem(), true);
					itemResource.setValue(tenderItem.getValue());
					itemResource.setValueMapped(tenderItem.getValue());
					tenderResource.getItems().add(itemResource);
				}
			}

			result.add(tenderResource);
		}

		return result;
	}

	@Transactional
	public TenderResource find(AppUserDetails principal, Integer id) {
		Tender entity = tenderRepository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(String.format("Tender [%s] not found", id));
		}

		User user = userRepository.findOne(principal.getUser().getId());
		List<Project> projects = companyInvestmentRepository.findByCompany(user.getCompany());
		algorithmService.processTender4Investments(entity, projects);

		Impression impression = new Impression();
		impression.setEntityType(EntityType.TENDER);
		impression.setEntityId(entity.getId());
		impressionRepository.save(impression);

		TenderResource resource = tenderResourceAssembler.toResource(entity, false);
		Collections.sort(resource.getProjects(), new Comparator<ProjectResource>() {
			@Override
			public int compare(ProjectResource o1, ProjectResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return resource;
	}

}
