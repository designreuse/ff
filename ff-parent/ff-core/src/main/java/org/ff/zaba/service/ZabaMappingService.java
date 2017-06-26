package org.ff.zaba.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.repository.ItemOptionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZabaMappingService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	private Map<String, Integer> legalTypes;

	private Map<String, Integer> bankruptcyProcedures;

	private Map<Boolean, Integer> blocked5Days;

	private Map<Boolean, Integer> blocked20Days;

	private Map<Boolean, Integer> profitBeforeTax;

	private Map<Boolean, Integer> profitOrLoss;

	private Map<Boolean, Integer> capitalTotalLiabilities;

	private Double capitalTotalLiabilitiesThreshold;

	@PostConstruct
	public void init() {
		try {
			initMappingLegalTypes();
			initMappingBankruptcyProcedure();
			initMappingBlocked5Days();
			initMappingBlocked20Days();
			initProfitBeforeTax();
			initProfitOrLoss();
			initCapitalTotalLiabilities();

			log.debug("==================== C O M P A N Y    D A T A    M A P P I N G S ====================");
			log.debug("legalTypes: {}", legalTypes);
			log.debug("zipCode: {}", baseProperties.getMappingZipCode());
			log.debug("foundingDate: {}", baseProperties.getMappingFoundingDate());
			log.debug("bankruptcyProcedures: {}", bankruptcyProcedures);
			log.debug("blocked5Days: {}", blocked5Days);
			log.debug("blocked20Days: {}", blocked20Days);
			log.debug("numberOfEmployees: {}", baseProperties.getMappingNumberOfEmployees());
			log.debug("lastYearIncome: {}", baseProperties.getMappingLastYearIncome());
			log.debug("profitBeforeTax: {}", profitBeforeTax);
			log.debug("profitOrLoss: {}", profitOrLoss);
			log.debug("capitalTotalLiabilities: {} {}", capitalTotalLiabilitiesThreshold, capitalTotalLiabilities);
			log.debug("=======================================================================================");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void initMappingLegalTypes() throws Exception {
		legalTypes = new HashMap<>();
		String mapping = baseProperties.getMappingLegalTypeNumber();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				String[] array = str.split("\\-");
				legalTypes.put(array[0], Integer.parseInt(array[1]));
			}
		}
	}

	private void initMappingBankruptcyProcedure() throws Exception {
		bankruptcyProcedures = new HashMap<>();
		String mapping = baseProperties.getMappingBankruptcyProcedure();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				String[] array = str.split("\\-");
				bankruptcyProcedures.put(array[0], Integer.parseInt(array[1]));
			}
		}
	}

	private void initMappingBlocked5Days() throws Exception {
		blocked5Days = new HashMap<>();
		String mapping = baseProperties.getMappingBlocked5Days();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				String[] array = str.split("\\-");
				blocked5Days.put(Boolean.valueOf(array[0]), Integer.parseInt(array[1]));
			}
		}
	}

	private void initMappingBlocked20Days() throws Exception {
		blocked20Days = new HashMap<>();
		String mapping = baseProperties.getMappingBlocked20Days();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				String[] array = str.split("\\-");
				blocked20Days.put(Boolean.valueOf(array[0]), Integer.parseInt(array[1]));
			}
		}
	}

	private void initProfitBeforeTax() throws Exception {
		profitBeforeTax = new HashMap<>();
		String mapping = baseProperties.getMappingProfitBeforeTax();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				String[] array = str.split("\\-");
				profitBeforeTax.put(Boolean.valueOf(array[0]), Integer.parseInt(array[1]));
			}
		}
	}

	private void initProfitOrLoss() throws Exception {
		profitOrLoss = new HashMap<>();
		String mapping = baseProperties.getMappingProfitOrLoss();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				String[] array = str.split("\\-");
				profitOrLoss.put(Boolean.valueOf(array[0]), Integer.parseInt(array[1]));
			}
		}
	}

	private void initCapitalTotalLiabilities() throws Exception {
		capitalTotalLiabilities = new HashMap<>();
		String mapping = baseProperties.getMappingCapitalTotalLiabilities();
		if (StringUtils.isNotBlank(mapping)) {
			for (String str : mapping.split("\\|")) {
				if (str.contains("-")) {
					String[] array = str.split("\\-");
					capitalTotalLiabilities.put(Boolean.valueOf(array[0]), Integer.parseInt(array[1]));
				} else {
					capitalTotalLiabilitiesThreshold = Double.parseDouble(str);
				}
			}
		}
	}

	/**
	 * Method returns ItemOption for the given external value.
	 * @param value
	 * 		External value for the 'Legal type number'.
	 * @return
	 * 		ItemOption if found, null otherwise.
	 */
	public ItemOption getLegalTypeNumberMapping(String value) {
		if (!legalTypes.containsKey(value)) {
			log.warn("Unknown external value [{}] detected for legal type number", value);
			return null;
		}
		return itemOptionRepository.findOne(legalTypes.get(value));
	}

	/**
	 * Method returns Item that indicates where (location) the company is founded.
	 * @return
	 */
	public Item getZipCodeMapping() {
		return itemRepository.findOne(baseProperties.getMappingZipCode());
	}

	/**
	 * Method returns Item that indicates when (date) the company is founded.
	 * @return
	 */
	public Item getFoundingDateMapping() {
		return itemRepository.findOne(baseProperties.getMappingFoundingDate());
	}

	/**
	 * Method returns ItemOption for the given external value.
	 * @param value
	 * 		External value for the 'Bankruptcy procedure' (e.g. S, L, P).
	 * @return
	 */
	public ItemOption getBankruptcyProcedureMapping(String value) {
		if (bankruptcyProcedures.containsKey(value)) {
			return itemOptionRepository.findOne(bankruptcyProcedures.get(value));
		} else {
			return itemOptionRepository.findOne(bankruptcyProcedures.get("?"));
		}
	}

	/**
	 * Method returns ItemOption for the given external value.
	 * @param value
	 * 		External value for the 'Is blocked for 5 days' (e.g. true/false).
	 * @return
	 */
	public ItemOption getBlocked5DaysMapping(Boolean value) {
		return itemOptionRepository.findOne(blocked5Days.get(value));
	}

	/**
	 * Method returns ItemOption for the given external value.
	 * @param value
	 * 		External value for the 'Is blocked for 20 days' (e.g. true/false).
	 * @return
	 */
	public ItemOption getBlocked20DaysMapping(Boolean value) {
		return itemOptionRepository.findOne(blocked20Days.get(value));
	}

	/**
	 * Method returns Item that indicates the number of employees.
	 * @return
	 */
	public Item getNumberOfEmployeesMapping() {
		return itemRepository.findOne(baseProperties.getMappingNumberOfEmployees());
	}

	/**
	 * Method returns Item that indicates the last year income.
	 * @return
	 */
	public Item getLastYearIncomeMapping() {
		return itemRepository.findOne(baseProperties.getMappingLastYearIncome());
	}

	/**
	 * Method returns ItemOption for the given external value.
	 * @param value
	 * 		External value for the 'Is profit before tax' (e.g. 625913.00).
	 * @return
	 */
	public ItemOption getProfitBeforeTaxMapping(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		Double d = Double.parseDouble(value);
		if (d.doubleValue() < 0) {
			return itemOptionRepository.findOne(profitBeforeTax.get(Boolean.FALSE));
		} else {
			return itemOptionRepository.findOne(profitBeforeTax.get(Boolean.TRUE));
		}
	}

	/**
	 * Method returns ItemOption for the given external value.
	 * @param value
	 * 		External value for the 'Profit or loss' (e.g. 625913.00).
	 * @return
	 */
	public ItemOption getProfitOrLossMapping(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		Double d = Double.parseDouble(value);
		if (d.doubleValue() < 0) {
			return itemOptionRepository.findOne(profitOrLoss.get(Boolean.FALSE));
		} else {
			return itemOptionRepository.findOne(profitOrLoss.get(Boolean.TRUE));
		}
	}

	/**
	 * Method returns ItemOption for the given external values.
	 * @param capital
	 * 		External value for the 'Capital' (e.g. 4642015.00).
	 * @param totalLiabilities
	 * 		External value for the 'Total liabilities' (e.g. 11476427.00).
	 * @return
	 */
	public ItemOption getCapitalTotalLiabilities(String capital, String totalLiabilities) {
		if (StringUtils.isBlank(capital) || StringUtils.isBlank(totalLiabilities)) {
			return null;
		}

		Double dCapital = Double.parseDouble(capital);
		Double dTotalLiabilities = Double.parseDouble(totalLiabilities);

		Double result = dCapital / dTotalLiabilities;

		if (result >= capitalTotalLiabilitiesThreshold) {
			return itemOptionRepository.findOne(capitalTotalLiabilities.get(Boolean.TRUE));
		} else {
			return itemOptionRepository.findOne(capitalTotalLiabilities.get(Boolean.FALSE));
		}
	}

}
