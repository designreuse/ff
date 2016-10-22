package org.ff.tender.service;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.resource.image.ImageResourceAssembler;
import org.ff.resource.item.ItemResource;
import org.ff.resource.item.ItemResourceAssembler;
import org.ff.resource.tender.TenderResource;
import org.ff.resource.tender.TenderResourceAssembler;
import org.ff.service.algorithm.AlgorithmService;
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
