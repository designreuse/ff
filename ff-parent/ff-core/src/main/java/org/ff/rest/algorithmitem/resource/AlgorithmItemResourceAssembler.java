package org.ff.rest.algorithmitem.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.AlgorithmItem;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemStatus;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemType;
import org.ff.jpa.domain.AlgorithmItem.Operator;
import org.ff.jpa.repository.ItemRepository;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmItemResourceAssembler {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	public AlgorithmItemResource toResource(AlgorithmItem entity, boolean light) {
		AlgorithmItemResource resource = new AlgorithmItemResource();
		resource.setId(entity.getId());
		resource.setCode(entity.getCode());
		resource.setType(entity.getType());
		resource.setStatus(entity.getStatus());
		resource.setCompanyItem(itemResourceAssembler.toResource(entity.getCompanyItem(), light));
		resource.setTenderItem(itemResourceAssembler.toResource(entity.getTenderItem(), light));
		resource.setOperator(new OperatorResource(entity.getOperator().name(), entity.getOperator().name()));
		resource.setConditionalItemCode(entity.getConditionalItemCode());
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<AlgorithmItemResource> toResources(Iterable<AlgorithmItem> entities, boolean light) {
		List<AlgorithmItemResource> resources = new ArrayList<>();
		for (AlgorithmItem entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public AlgorithmItem createEntity(AlgorithmItemResource resource) {
		AlgorithmItem entity = new AlgorithmItem();
		entity.setCode(resource.getCode());
		entity.setType(resource.getType());
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : AlgorithmItemStatus.INACTIVE);
		entity.setCompanyItem(itemRepository.findOne(resource.getCompanyItem().getId()));
		entity.setTenderItem(itemRepository.findOne(resource.getTenderItem().getId()));
		entity.setOperator(Operator.valueOf(resource.getOperator().getValue()));
		entity.setConditionalItemCode((resource.getType() == AlgorithmItemType.CONDITIONAL) ? resource.getConditionalItemCode() : null);
		return entity;
	}

	public AlgorithmItem updateEntity(AlgorithmItem entity, AlgorithmItemResource resource) {
		entity.setCode(resource.getCode());
		entity.setType(resource.getType());
		entity.setStatus(resource.getStatus());
		entity.setCompanyItem(itemRepository.findOne(resource.getCompanyItem().getId()));
		entity.setTenderItem(itemRepository.findOne(resource.getTenderItem().getId()));
		entity.setOperator(Operator.valueOf(resource.getOperator().getValue()));
		entity.setConditionalItemCode((resource.getType() == AlgorithmItemType.CONDITIONAL) ? resource.getConditionalItemCode() : null);
		return entity;
	}

}
