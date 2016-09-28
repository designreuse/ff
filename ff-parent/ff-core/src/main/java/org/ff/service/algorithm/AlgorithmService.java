package org.ff.service.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.etm.EtmService;
import org.ff.jpa.domain.AlgorithmItem;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemStatus;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemType;
import org.ff.jpa.domain.AlgorithmItem.Operator;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.AlgorithmItemRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlgorithmService extends BaseService {

	@Autowired
	private EtmService etmService;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private AlgorithmItemRepository algorithmItemRepository;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	public List<Tender> findTenders4User(User user) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findTenders4User");

		List<Tender> result = new ArrayList<>();
		Company company = user.getCompany();

		try {
			log.debug("Find tenders for {} [{}]...", company.getName(), company.getId());

			// active algorithm items
			List<AlgorithmItem> algorithmItems = algorithmItemRepository.findByStatusOrderByCode(AlgorithmItemStatus.ACTIVE);

			// company items
			Set<CompanyItem> companyItems = company.getItems();

			// active tenders
			Iterable<Tender> tenders = tenderRepository.findByStatus(TenderStatus.ACTIVE);

			for (Tender tender : tenders) {
				Map<AlgorithmItem, Boolean> match4AlgorithmItem = new HashMap<>();

				for (AlgorithmItem algorithmItem : algorithmItems) {
					if (processAlgorithmItem(tender, companyItems, algorithmItem) == Boolean.TRUE) {
						match4AlgorithmItem.put(algorithmItem, Boolean.TRUE);
						log.debug("Algorithm item [{}]: match", algorithmItem.getCode());
					} else {
						match4AlgorithmItem.put(algorithmItem, Boolean.FALSE);
						log.debug("Algorithm item [{}]: no match", algorithmItem.getCode());
					}
				}

				// conditional item check
				for (Entry<AlgorithmItem, Boolean> entry : match4AlgorithmItem.entrySet()) {
					if (entry.getValue() == Boolean.FALSE && entry.getKey().getType() == AlgorithmItemType.CONDITIONAL) {
						if (StringUtils.isNotBlank(entry.getKey().getConditionalItemCode())) {
							log.debug("Initiating conditional processing for algorithm item [{}]...", entry.getKey().getCode());
							AlgorithmItem conditionalItem = algorithmItemRepository.findByCode(entry.getKey().getConditionalItemCode());
							if (match4AlgorithmItem.get(conditionalItem) == Boolean.TRUE) {
								match4AlgorithmItem.put(entry.getKey(), Boolean.TRUE);
								log.debug("Conditional algorithm item [{}]: match", conditionalItem.getCode());
							}
						}
					}
				}

				boolean match4Tender = true;
				for (Entry<AlgorithmItem, Boolean> entry : match4AlgorithmItem.entrySet()) {
					if (entry.getValue() == Boolean.FALSE && entry.getKey().getType() != AlgorithmItemType.OPTIONAL) {
						match4Tender = false;
					}
				}

				if (match4Tender) {
					result.add(tender);
				}
			}

			return result;
		} finally {
			etmService.collect(point);
			log.debug("{} tender(s) found in {} ms for {} [{}]", result.size(), point.getTransactionTime(), company.getName(), company.getId());
		}
	}

	private Boolean processAlgorithmItem(Tender tender, Set<CompanyItem> companyItems, AlgorithmItem algorithmItem) {
		CompanyItem companyItem = getCompanyItem4Item(companyItems, algorithmItem.getCompanyItem());
		TenderItem tenderItem = getTenderItem4Item(tender.getItems(), algorithmItem.getTenderItem());

		if (companyItem != null && tenderItem != null) {
			if (companyItem.getItem().getType() == ItemType.RADIO && algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.CHECKBOX) {
				return radioInCheckbox(companyItem, tenderItem);
			} else if (companyItem.getItem().getType() == ItemType.SELECT && algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.MULTISELECT) {
				return selectInMultiselect(companyItem, tenderItem);
			} else if (companyItem.getItem().getType() == ItemType.NKD && algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.NKDS) {
				return nkdInNkds(companyItem, tenderItem);
			} else if (companyItem.getItem().getType() == ItemType.NKDS && algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.NKDS) {
				return nkdsInNkds(companyItem, tenderItem);
			}
		}

		return Boolean.FALSE;
	}

	private CompanyItem getCompanyItem4Item(Set<CompanyItem> companyItems, Item item) {
		for (CompanyItem companyItem : companyItems) {
			if (companyItem.getItem().getId().equals(item.getId())) {
				return companyItem;
			}
		}
		return null;
	}

	private TenderItem getTenderItem4Item(Set<TenderItem> tenderItems, Item item) {
		for (TenderItem tenderItem : tenderItems) {
			if (tenderItem.getItem().getId().equals(item.getId())) {
				return tenderItem;
			}
		}
		return null;
	}

	private Boolean radioInCheckbox(CompanyItem companyItem, TenderItem tenderItem) {
		if (StringUtils.isNotBlank(tenderItem.getValue())) {
			String companyItemValue = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText();
			for (String itemOptionId : tenderItem.getValue().split("\\|")) {
				String tenderItemValue = itemOptionRepository.findOne(Integer.parseInt(itemOptionId)).getText();
				if (companyItemValue.equals(tenderItemValue)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private Boolean selectInMultiselect(CompanyItem companyItem, TenderItem tenderItem) {
		if (StringUtils.isNotBlank(companyItem.getValue())) {
			String companyItemValue = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText();
			if (StringUtils.isNotBlank(tenderItem.getValue())) {
				for (String itemOptionId : tenderItem.getValue().split("\\|")) {
					String tenderItemValue = itemOptionRepository.findOne(Integer.parseInt(itemOptionId)).getText();
					if (companyItemValue.equals(tenderItemValue)) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

	private Boolean nkdInNkds(CompanyItem companyItem, TenderItem tenderItem) {
		if (StringUtils.isNotBlank(companyItem.getValue()) && StringUtils.isNotBlank(tenderItem.getValue())) {
			// companyItemValue is NKD ID (e.g. 457); tenderItemValue is pipe-separated list of NKD IDs (e.g. 457|458|459|460)
			for (String str : tenderItem.getValue().split("\\|")) {
				if (companyItem.getValue().equals(str)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private Boolean nkdsInNkds(CompanyItem companyItem, TenderItem tenderItem) {
		if (StringUtils.isNotBlank(companyItem.getValue()) && StringUtils.isNotBlank(tenderItem.getValue())) {
			for (String str1 : companyItem.getValue().split("\\|")) {
				for (String str2 : tenderItem.getValue().split("\\|")) {
					if (str1.equals(str2)) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

}
