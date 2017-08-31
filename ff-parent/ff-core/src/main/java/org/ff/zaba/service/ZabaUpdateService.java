package org.ff.zaba.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.domain.ZabaMappingsLocation;
import org.ff.jpa.repository.CompanyItemRepository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.jpa.repository.ZabaMappingsLocationRepository;
import org.ff.zaba.resource.ZabaCompanyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZabaUpdateService {

	@Autowired
	private ZabaMappingService zabaMappingService;

	@Autowired
	private CompanyItemRepository companyItemRepository;

	@Autowired
	private ZabaMappingsLocationRepository zabaMappingsLocationRepository;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public void updateCompanyData(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		log.debug("Initiating update of company data...");

		updateLegalTypeNumber(company, resource, forceUpdate);
		updateZipCode(company, resource, forceUpdate);
		updateFoundingDate(company, resource, forceUpdate);
		updateBankruptcyProcedure(company, resource, forceUpdate);
		updateBlocked5Days(company, resource, forceUpdate);
		updateBlocked20Days(company, resource, forceUpdate);
		updateNumberOfEmployees(company, resource, forceUpdate);
		updateLastYearIncome(company, resource, forceUpdate);
		updateProfitBeforeTax(company, resource, forceUpdate);
		updateProfitOrLoss(company, resource, forceUpdate);
		updateCapitalTotalLiabilities(company, resource, forceUpdate);
	}

	private void updateLegalTypeNumber(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating legal type number [{}] for company [{}]", resource.getLegalTypeNumber(), company.getName());

			ItemOption itemOption = zabaMappingService.getLegalTypeNumberMapping(resource.getLegalTypeNumber());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Legal type number successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating legal type number failed", e);
		}
	}

	private void updateZipCode(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating ZIP code [{}] for company [{}]", resource.getZipCode(), company.getName());

			Item item = zabaMappingService.getZipCodeMapping();
			if (StringUtils.isNotBlank(resource.getZipCode())) {
				ZabaMappingsLocation zabaMappingsLocation = zabaMappingsLocationRepository.findByZipCode(resource.getZipCode()).get(0);
				if (zabaMappingsLocation != null) {
					Subdivision2 subdivision2 = subdivision2Repository.findByName(zabaMappingsLocation.getSubdivision2());
					if (subdivision2 != null) {
						CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
						if (companyItem == null) {
							companyItem = new CompanyItem();
							companyItem.setValue(subdivision2.getId().toString());
						}
						companyItem.setCompany(company);
						companyItem.setItem(item);
						companyItem.setValueExt(subdivision2.getId().toString());
						if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
							companyItem.setValue(companyItem.getValueExt());
						}
						companyItemRepository.save(companyItem);

						log.debug("ZIP code successfully imported - company item [id: {}, value: {}]", companyItem.getId(), subdivision2.getName());
					}
				}
			}
		} catch (Exception e) {
			log.error("Updating ZIP code failed", e);
		}
	}

	private void updateFoundingDate(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating founding date [{}] for company [{}]", resource.getFoundingDate(), company.getName());

			Item item = zabaMappingService.getFoundingDateMapping();
			if (resource.getFoundingDate() != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(dateFormat.format(resource.getFoundingDate()));
				}
				companyItem.setCompany(company);
				companyItem.setItem(item);
				companyItem.setValueExt(dateFormat.format(resource.getFoundingDate()));
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Founding date successfully imported - company item [id: {}, value: {}]", companyItem.getId(), companyItem.getValue());
			}
		} catch (Exception e) {
			log.error("Updating founding date failed", e);
		}
	}

	private void updateBankruptcyProcedure(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating bankruptcy procedure [{}] for company [{}]", resource.getAnyBankruptcyProcedure(), company.getName());

			ItemOption itemOption = zabaMappingService.getBankruptcyProcedureMapping(resource.getAnyBankruptcyProcedure());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Bankruptcy procedure successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating bankruptcy procedure failed", e);
		}
	}

	private void updateBlocked5Days(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating blocked 5 days [{}] for company [{}]", resource.getIsBlockedFor5Days(), company.getName());

			ItemOption itemOption = zabaMappingService.getBlocked5DaysMapping(resource.getIsBlockedFor5Days());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Blocked 5 days successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating blocked 5 days failed", e);
		}
	}

	private void updateBlocked20Days(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating blocked 20 days [{}] for company [{}]", resource.getIsBlockedFor20Days(), company.getName());

			ItemOption itemOption = zabaMappingService.getBlocked20DaysMapping(resource.getIsBlockedFor20Days());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Blocked 20 days successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating blocked 20 days failed", e);
		}
	}

	private void updateNumberOfEmployees(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating number of employees [{}] for company [{}]", resource.getNumberOfEmployeesAnnual(), company.getName());

			Item item = zabaMappingService.getNumberOfEmployeesMapping();
			if (resource.getNumberOfEmployeesAnnual() != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(resource.getNumberOfEmployeesAnnual());
				}
				companyItem.setCompany(company);
				companyItem.setItem(item);
				companyItem.setValueExt(resource.getNumberOfEmployeesAnnual());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Number of employees successfully imported - company item [id: {}, value: {}]", companyItem.getId(), companyItem.getValue());
			}
		} catch (Exception e) {
			log.error("Updating number of employees failed", e);
		}
	}

	private void updateLastYearIncome(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating last year income [{}] for company [{}]", resource.getLastYearIncome(), company.getName());

			if (resource.getLastYearIncome() != null) {
				Item item = zabaMappingService.getLastYearIncomeMapping();
				Map<ItemOption, DoubleRange> ranges = new HashMap<>();
				for (ItemOption itemOption : item.getOptions()) {
					String value = itemOption.getText();
					value = value.substring(0, value.indexOf(" ")).trim().replace(".", "");
					if (value.contains("-")) {
						String[] values = value.split("\\-");
						ranges.put(itemOption, new DoubleRange(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
					} else if (value.contains("<")) {
						value = value.substring(value.indexOf("<") + 1);
						ranges.put(itemOption, new DoubleRange(Double.MIN_VALUE, Double.parseDouble(value)));
					} else if (value.contains(">")) {
						value = value.substring(value.indexOf(">") + 1);
						ranges.put(itemOption, new DoubleRange(Double.parseDouble(value), Double.MAX_VALUE));
					}
				}

				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
				boolean newCompanyItem = false;
				if (companyItem == null) {
					companyItem = new CompanyItem();
					newCompanyItem = true;
				}
				companyItem.setCompany(company);
				companyItem.setItem(item);

				ItemOption itemOption = null;
				Double value = Double.valueOf(resource.getLastYearIncome());
				for (Entry<ItemOption, DoubleRange> range : ranges.entrySet()) {
					if (range.getValue().containsDouble(value)) {
						itemOption = range.getKey();
						companyItem.setValueExt(itemOption.getId().toString());
						if (newCompanyItem || forceUpdate || Boolean.TRUE == company.getSyncData()) {
							companyItem.setValue(companyItem.getValueExt());
						}
						break;
					}
				}

				companyItemRepository.save(companyItem);

				log.debug("Last year income successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating last year income failed", e);
		}
	}

	private void updateProfitBeforeTax(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating profit before tax [{}] for company [{}]", resource.getIsProfitBeforeTax(), company.getName());

			ItemOption itemOption = zabaMappingService.getProfitBeforeTaxMapping(resource.getIsProfitBeforeTax());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Profit before tax successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating profit before tax failed", e);
		}
	}

	private void updateProfitOrLoss(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating profit or loss [{}] for company [{}]", resource.getProfitOrLoss(), company.getName());

			ItemOption itemOption = zabaMappingService.getProfitOrLossMapping(resource.getProfitOrLoss());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Profit or loss successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating profit or loss failed", e);
		}
	}

	private void updateCapitalTotalLiabilities(Company company, ZabaCompanyResource resource, boolean forceUpdate) {
		try {
			log.debug("Updating capital [{}] / total liabilities [{}] for company [{}]", resource.getCapital(), resource.getTotalLiabilities(), company.getName());

			ItemOption itemOption = zabaMappingService.getCapitalTotalLiabilities(resource.getCapital(), resource.getTotalLiabilities());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
					companyItem.setValue(itemOption.getId().toString());
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValueExt(itemOption.getId().toString());
				if (forceUpdate || Boolean.TRUE == company.getSyncData()) {
					companyItem.setValue(companyItem.getValueExt());
				}
				companyItemRepository.save(companyItem);

				log.debug("Capital / total liabilities successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Updating capital / total liabilities failed", e);
		}
	}

}
