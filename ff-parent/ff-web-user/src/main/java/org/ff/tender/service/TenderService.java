package org.ff.tender.service;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.company.CompanyResource;
import org.ff.resource.company.CompanyResourceAssembler;
import org.ff.resource.image.ImageResourceAssembler;
import org.ff.resource.item.ItemResource;
import org.ff.resource.item.ItemResourceAssembler;
import org.ff.resource.tender.TenderResource;
import org.ff.resource.tender.TenderResourceAssembler;
import org.ff.service.algorithm.AlgorithmService;
import org.ff.tender.resource.DemoResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenderService {

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

	@Transactional(readOnly = true)
	public List<TenderResource> findAll(UserDetails principal) {
		List<TenderResource> result = new ArrayList<>();

		for (Tender tender : algorithmService.findTenders4User(userRepository.findByEmail(principal.getUsername()))) {
			TenderResource resource = new TenderResource();
			resource.setId(tender.getId());
			resource.setStatus(tender.getStatus());
			resource.setName(tender.getName());
			resource.setText(tender.getText());
			if (tender.getImage() != null) {
				resource.setImage(imageResourceAssembler.toResource(tender.getImage()));
			}
			resource.setCreationDate(tender.getCreationDate().toDate());
			resource.setCreatedBy(tender.getCreatedBy());
			resource.setLastModifiedDate(tender.getLastModifiedDate().toDate());
			resource.setLastModifiedBy(tender.getLastModifiedBy());

			resource.setItems(new ArrayList<ItemResource>());
			for (TenderItem tenderItem : tender.getItems()) {
				if (Boolean.TRUE == tenderItem.getItem().getWidgetItem()) {
					ItemResource itemResource = itemResourceAssembler.toResource(tenderItem.getItem(), true);
					itemResource.setValue(tenderItem.getValue());
					itemResource.setValueMapped(tenderItem.getValue());
					resource.getItems().add(itemResource);
				}
			}

			result.add(resource);
		}

		return result;
	}

	@Transactional(readOnly = true)
	public List<TenderResource> findAllDemo(UserDetails principal, DemoResource demoResource) {
		List<TenderResource> result = new ArrayList<>();

		if (demoResource.getCompany() == null) {
			return result;
		}

		User user = userRepository.findByEmail(principal.getUsername());

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

		for (Tender tender : algorithmService.findTenders4User(user)) {
			TenderResource tenderResource = new TenderResource();
			tenderResource.setId(tender.getId());
			tenderResource.setStatus(tender.getStatus());
			tenderResource.setName(tender.getName());
			tenderResource.setText(tender.getText());
			if (tender.getImage() != null) {
				tenderResource.setImage(imageResourceAssembler.toResource(tender.getImage()));
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
	public TenderResource find(Integer id) {
		Tender entity = tenderRepository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(String.format("Tender [%s] not found", id));
		}

		Impression impression = new Impression();
		impression.setEntityType(EntityType.TENDER);
		impression.setEntityId(entity.getId());
		impressionRepository.save(impression);

		return tenderResourceAssembler.toResource(entity, false);
	}

}
