package org.ff.common.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.service.BaseService;
import org.ff.common.etm.EtmService;
import org.ff.jpa.domain.AlgorithmItem;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemStatus;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemType;
import org.ff.jpa.domain.AlgorithmItem.Operator;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Project;
import org.ff.jpa.domain.ProjectItem;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.AlgorithmItemRepository;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.debugging.resource.DebuggingEntry;
import org.ff.rest.debugging.resource.DebuggingEntry.Status;
import org.ff.rest.debugging.resource.DebuggingResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.joda.time.LocalDate;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Subdivision1Repository subdivision1Repository;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	@Autowired
	private ProjectRepository projectRepository;

	public boolean isMatch(User user, Company company, List<Project> companyProjects, Tender tender, DebuggingResource debug) {
		if (user.getCompany() == null) {
			debug.add(new DebuggingEntry(new Date(), String.format("Company not found"), Status.NOK));
			return false;
		}

		// company items
		Set<CompanyItem> companyItems = company.getItems();

		// tender investments
		List<Integer> tenderInvestments = getTenderInvestments(tender);

		// check if company investments match tender investments
		if (!processCompany4Investments(companyProjects, tenderInvestments, debug)) {
			return false;
		}

		Map<AlgorithmItem, Boolean> match4AlgorithmItem = new HashMap<>();

		for (AlgorithmItem algorithmItem : algorithmItemRepository.findByStatusOrderByCode(AlgorithmItemStatus.ACTIVE)) {
			if (processAlgorithmItem(tender, companyItems, companyProjects, algorithmItem, debug) == Boolean.TRUE) {
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

		for (Entry<AlgorithmItem, Boolean> entry : match4AlgorithmItem.entrySet()) {
			if (entry.getValue() == Boolean.FALSE && entry.getKey().getType() != AlgorithmItemType.OPTIONAL) {
				return false;
			}
		}

		return true;
	}

	public List<User> findUsers4Tender(Tender tender, DebuggingResource debug) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findUsers4Tender");

		List<User> result = new ArrayList<>();

		try {
			log.debug("Find users for tender [{}]...", tender.getId());

			for (User user : userRepository.findByStatus(UserStatus.ACTIVE)) {
				if (isMatch(user, user.getCompany(), projectRepository.findByCompany(user.getCompany()), tender, debug)) {
					result.add(user);
				}
			}

			return result;
		} finally {
			etmService.collect(point);
			log.debug("{} users found in {} ms for tender [{}]", result.size(), point.getTransactionTime(), tender.getId());
		}
	}

	public List<Tender> findTenders4User(User user, Company company, List<Project> companyProjects, DebuggingResource debug) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findTenders4User");

		List<Tender> result = new ArrayList<>();

		try {
			log.debug("Find tenders for {} [{}]...", company.getName(), company.getId());

			for (Tender tender : tenderRepository.findByStatus(TenderStatus.ACTIVE)) {
				if (isMatch(user, company, companyProjects, tender, debug)) {
					result.add(tender);
				}
			}

			return result;
		} finally {
			etmService.collect(point);
			log.debug("{} tender(s) found in {} ms for {} [{}]", result.size(), point.getTransactionTime(), company.getName(), company.getId());
		}
	}

	private Boolean processCompany4Investments(List<Project> projects, List<Integer> tenderInvestments, DebuggingResource debug) {
		if (tenderInvestments.isEmpty()) {
			// tender is not restricted to any particular investment
			debug.add(new DebuggingEntry(new Date(), String.format("Tender is not restricted to any particular investment"), Status.OK));
			return Boolean.TRUE;
		}

		for (Project project : projects) {
			for (Investment investment : project.getInvestments()) {
				if (tenderInvestments.contains(investment.getId())) {
					// company has at least one investment required by the tender
					debug.add(new DebuggingEntry(new Date(), String.format("Company has at least one investment [%s] required by the tender", investment.getName()), Status.OK));
					return Boolean.TRUE;
				}
			}
		}

		debug.add(new DebuggingEntry(new Date(), String.format("Company has no investment required by the tender"), Status.NOK));
		return Boolean.FALSE;
	}

	public Boolean processTender4Projects(Tender tender, List<Project> projects) {
		List<Integer> tenderInvestments = getTenderInvestments(tender);

		if (tenderInvestments.isEmpty()) {
			// tender is not restricted to any particular investment
			return Boolean.TRUE;
		}

		for (Project project : projects) {
			for (Investment investment : project.getInvestments()) {
				if (tenderInvestments.contains(investment.getId())) {
					tender.getProjects().add(project);
				}
			}
		}

		return (tender.getProjects().isEmpty()) ? Boolean.FALSE : Boolean.TRUE;
	}

	private List<Integer> getTenderInvestments(Tender tender) {
		List<Integer> tenderInvestments = new ArrayList<>();

		for (TenderItem tenderItem : tender.getItems()) {
			if (ItemType.INVESTMENTS_PRIMARY == tenderItem.getItem().getType()) {
				if (StringUtils.isNotBlank(tenderItem.getValue())) {
					for (String investmentId : tenderItem.getValue().split("\\|")) {
						tenderInvestments.add(Integer.parseInt(investmentId));
					}
				}
				break;
			}
		}

		return tenderInvestments;
	}

	private Boolean processAlgorithmItem(Tender tender, Set<CompanyItem> companyItems, List<Project> companyInvestments, AlgorithmItem algorithmItem, DebuggingResource debug) {
		CompanyItem companyItem = getCompanyItem4Item(companyItems, algorithmItem.getCompanyItem());
		TenderItem tenderItem = getTenderItem4Item(tender.getItems(), algorithmItem.getTenderItem());

		if (companyItem == null) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s] >>> company item [%s - %s] not found", algorithmItem.getCode(), algorithmItem.getCompanyItem().getCode(),  algorithmItem.getCompanyItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (tenderItem == null) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s] >>> tender item [%s - %s] not found", algorithmItem.getCode(), algorithmItem.getTenderItem().getCode(),  algorithmItem.getTenderItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (algorithmItem.getCompanyItem().getMetaTag() != null && ProjectResourceAssembler.getCompanyInvestmentMetaTags().contains(algorithmItem.getCompanyItem().getMetaTag())) {
			if (algorithmItem.getCompanyItem().getType() == ItemType.SUBDIVISION1 && algorithmItem.getOperator() == Operator.IN
					&& tenderItem.getItem().getType() == ItemType.MULTISELECT && tenderItem.getItem().getMetaTag() == ItemMetaTag.TENDER_DEVELOPMENT_INDEX_SUBDIVISION1) {
				for (Project companyInvestment : companyInvestments) {
					for (ProjectItem companyInvestmentItem : companyInvestment.getItems()) {
						if (companyInvestmentItem.getItem().getId().equals(algorithmItem.getCompanyItem().getId())) {
							if (subdivision1InMultiselect(companyInvestmentItem.getValue(), tenderItem.getValue())) {
								log.trace("[{}] match for company investment [{}]", algorithmItem.getCode(), companyInvestment.getName());
								debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
								return Boolean.TRUE;
							}
						}
					}
				}
			} else if (algorithmItem.getCompanyItem().getType() == ItemType.SUBDIVISION2 && algorithmItem.getOperator() == Operator.IN
					&& tenderItem.getItem().getType() == ItemType.MULTISELECT && tenderItem.getItem().getMetaTag() == ItemMetaTag.TENDER_DEVELOPMENT_INDEX_SUBDIVISION2) {
				for (Project companyInvestment : companyInvestments) {
					for (ProjectItem companyInvestmentItem : companyInvestment.getItems()) {
						if (companyInvestmentItem.getItem().getId().equals(algorithmItem.getCompanyItem().getId())) {
							if (subdivision2InMultiselect(companyInvestmentItem.getValue(), tenderItem.getValue())) {
								log.trace("[{}] match for company investment [{}]", algorithmItem.getCode(), companyInvestment.getName());
								debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
								return Boolean.TRUE;
							}
						}
					}
				}
			} else if (algorithmItem.getCompanyItem().getType() == ItemType.ACTIVITY
					&& algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.ACTIVITIES) {
				for (Project companyInvestment : companyInvestments) {
					log.trace("Processing company investment [{}]", companyInvestment.getName());
					for (ProjectItem companyInvestmentItem : companyInvestment.getItems()) {
						if (companyInvestmentItem.getItem().getId().equals(algorithmItem.getCompanyItem().getId())) {
							if (activityInActivities(companyInvestmentItem.getValue(), tenderItem.getValue())) {
								log.trace("[{}] match for company investment [{}]", algorithmItem.getCode(), companyInvestment.getName());
								debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
								return Boolean.TRUE;
							}
						}
					}
				}
			}

			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.NOK));
			return Boolean.FALSE;
		}

		if (companyItem != null && tenderItem != null) {
			if (companyItem.getItem().getType() == ItemType.RADIO && algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.CHECKBOX) {
				return radioInCheckbox(algorithmItem, companyItem, tenderItem, debug);
			} else if (companyItem.getItem().getType() == ItemType.SELECT && algorithmItem.getOperator() == Operator.IN && tenderItem.getItem().getType() == ItemType.MULTISELECT) {
				return selectInMultiselect(algorithmItem, companyItem, tenderItem, debug);
			} else if (companyItem.getItem().getType() == ItemType.SELECT && algorithmItem.getOperator() == Operator.EQUAL && tenderItem.getItem().getType() == ItemType.SELECT) {
				return selectEqualSelect(algorithmItem, companyItem, tenderItem, debug);
			} else if (companyItem.getItem().getType() == ItemType.DATE && algorithmItem.getOperator() == Operator.GREATER_OR_EQUAL && tenderItem.getItem().getType() == ItemType.NUMBER) {
				return dateGreaterOrEqualNumber(algorithmItem, companyItem, tenderItem, debug);
			} else if (companyItem.getItem().getType() == ItemType.DATE && algorithmItem.getOperator() == Operator.LESS_OR_EQUAL && tenderItem.getItem().getType() == ItemType.NUMBER) {
				return dateLessOrEqualNumber(algorithmItem, companyItem, tenderItem, debug);
			} else if (companyItem.getItem().getType() == ItemType.NUMBER && algorithmItem.getOperator() == Operator.GREATER_OR_EQUAL && tenderItem.getItem().getType() == ItemType.NUMBER) {
				return numberGreaterOrEqualNumber(algorithmItem, companyItem, tenderItem, debug);
			} else if (companyItem.getItem().getType() == ItemType.NUMBER && algorithmItem.getOperator() == Operator.LESS_OR_EQUAL && tenderItem.getItem().getType() == ItemType.NUMBER) {
				return numberLessOrEqualNumber(algorithmItem, companyItem, tenderItem, debug);
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

	private Boolean subdivision1InMultiselect(String companyItemValue, String tenderItemValue) {
		if (StringUtils.isNotBlank(companyItemValue) && StringUtils.isNotBlank(tenderItemValue)) {
			Subdivision1 subdivision1 = subdivision1Repository.findOne(Integer.parseInt(companyItemValue));
			for (String str : tenderItemValue.split("\\|")) {
				ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(str));
				if (itemOption.getText().equals(subdivision1.getDevelopmentIndex())) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private Boolean subdivision2InMultiselect(String companyItemValue, String tenderItemValue) {
		if (StringUtils.isNotBlank(companyItemValue) && StringUtils.isNotBlank(tenderItemValue)) {
			Subdivision2 subdivision2 = subdivision2Repository.findOne(Integer.parseInt(companyItemValue));
			for (String str : tenderItemValue.split("\\|")) {
				ItemOption itemOption = itemOptionRepository.findOne(Integer.parseInt(str));
				if (itemOption.getText().equals(subdivision2.getDevelopmentIndex())) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private Boolean activityInActivities(String companyItemValue, String tenderItemValue) {
		if (StringUtils.isNotBlank(companyItemValue) && StringUtils.isNotBlank(tenderItemValue)) {
			// companyItemValue is activity ID (e.g. 1); tenderItemValue is pipe-separated list of activity IDs (e.g. 1|2|3|4)
			for (String str : tenderItemValue.split("\\|")) {
				if (companyItemValue.equals(str)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private Boolean radioInCheckbox(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		String companyItemValue = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText();
		for (String itemOptionId : tenderItem.getValue().split("\\|")) {
			String tenderItemValue = itemOptionRepository.findOne(Integer.parseInt(itemOptionId)).getText();
			if (companyItemValue.equals(tenderItemValue)) {
				debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
				return Boolean.TRUE;
			}
		}

		debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
		return Boolean.FALSE;
	}

	private Boolean selectInMultiselect(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		String companyItemValue = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText();
		for (String itemOptionId : tenderItem.getValue().split("\\|")) {
			String tenderItemValue = itemOptionRepository.findOne(Integer.parseInt(itemOptionId)).getText();
			if (companyItemValue.equals(tenderItemValue)) {
				debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
				return Boolean.TRUE;
			}
		}

		debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
		return Boolean.FALSE;

	}

	private Boolean selectEqualSelect(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		String companyItemValue = itemOptionRepository.findOne(Integer.parseInt(companyItem.getValue())).getText();
		String tenderItemValue = itemOptionRepository.findOne(Integer.parseInt(tenderItem.getValue())).getText();
		if (companyItemValue.equals(tenderItemValue)) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
			return Boolean.TRUE;
		} else {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
			return Boolean.FALSE;
		}
	}

	private Boolean dateGreaterOrEqualNumber(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		LocalDate date = new LocalDate(companyItem.getValue()).plusMonths(Integer.parseInt(tenderItem.getValue()));
		if (date.isBefore(new LocalDate())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
			return Boolean.TRUE;
		} else {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
			return Boolean.FALSE;
		}

	}

	private Boolean dateLessOrEqualNumber(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		LocalDate date = new LocalDate(companyItem.getValue()).plusMonths(Integer.parseInt(tenderItem.getValue()));
		if (date.isAfter(new LocalDate())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
			return Boolean.TRUE;
		} else {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
			return Boolean.FALSE;
		}
	}

	private Boolean numberGreaterOrEqualNumber(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (Integer.parseInt(companyItem.getValue()) >= Integer.parseInt(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
			return Boolean.TRUE;
		} else {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
			return Boolean.FALSE;
		}
	}

	private Boolean numberLessOrEqualNumber(AlgorithmItem algorithmItem, CompanyItem companyItem, TenderItem tenderItem, DebuggingResource debug) {
		if (StringUtils.isBlank(companyItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for company item [%s - %s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (StringUtils.isBlank(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value not set for tender item [%s - %s]", algorithmItem.getCode(), tenderItem.getItem().getCode(), tenderItem.getItem().getText()), Status.NOK));
			return Boolean.FALSE;
		}

		if (Integer.parseInt(companyItem.getValue()) <= Integer.parseInt(tenderItem.getValue())) {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]", algorithmItem.getCode()), Status.OK));
			return Boolean.TRUE;
		} else {
			debug.add(new DebuggingEntry(new Date(), String.format("Algorithm item [%s]; Value set for company item [%s - %s] does not match value set for tender item [%s - %s]; Operator [%s]", algorithmItem.getCode(), companyItem.getItem().getCode(), companyItem.getItem().getText(), tenderItem.getItem().getCode(), tenderItem.getItem().getText(), algorithmItem.getOperator()), Status.NOK));
			return Boolean.FALSE;
		}
	}

}
