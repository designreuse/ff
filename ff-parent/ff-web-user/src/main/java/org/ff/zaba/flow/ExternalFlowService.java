package org.ff.zaba.flow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.password.PasswordService;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.domain.ZabaMappingsLocation;
import org.ff.jpa.repository.CompanyItemRepository;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.jpa.repository.UserRepository;
import org.ff.jpa.repository.ZabaMappingsLocationRepository;
import org.ff.rest.user.resource.UserResource;
import org.ff.zaba.session.SessionClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalFlowService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private SessionClient sessionClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyItemRepository companyItemRepository;

	@Autowired
	private ExternalFlowMappingService externalFlowMappingService;

	@Autowired
	private PasswordService passwordGeneratorService;

	@Autowired
	private ZabaMappingsLocationRepository zabaMappingsLocationRepository;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	@Autowired
	private LogClientHttpRequestInterceptor interceptor;

	private RestTemplate restTemplate;

	private DateFormat dateFormat;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(interceptor);
		restTemplate.setInterceptors(interceptors);
		dateFormat = new SimpleDateFormat(baseProperties.getDateFormat());
	}

	public ResponseEntity<UserResource> authorize(String authId) {
		try {
			log.debug("Initiating external flow for authId [{}]...", authId);

			UserResource resource = new UserResource();

			// get external user authorization data from SESSION TRANSFER SERVICE
			Map<String, String> data = sessionClient.checkAuthId(authId);

			if (data.containsKey("matbr")) {
				// get company data from external source (e.g. from ZaBa) via ReST API
				ExternalCompanyResource companyData = getCompanyData(data.get("matbr"));

				log.debug("Company data: {}", companyData);

				// check if company is already registered in FundFinder
				Company company = companyRepository.findByCode(companyData.getOibNumber());
				User user = null;
				String password = passwordGeneratorService.generate();

				if (company == null) {
					user = new User();
					user.setStatus(UserStatus.ACTIVE);
					user.setFirstName(data.get("ime"));
					user.setLastName(data.get("prezime"));
					if (data.containsKey("email") && StringUtils.isNotBlank(data.get("email"))) {
						user.setEmail(data.get("email"));
					} else {
						user.setEmail(data.get("user_id"));
					}
					user.setPassword(PasswordService.encodePassword(password));
					user.setLastLoginDate(new DateTime());
					user.setDemoUser(Boolean.FALSE);
					user.setRegistrationType(UserRegistrationType.EXTERNAL);
					userRepository.save(user);

					log.debug("New user [{}] created", user.getId());

					company = new Company();
					company.setUser(user);
					company.setName(companyData.getSubjectName());
					company.setCode(companyData.getOibNumber());
					company.setRegistrationNumber(companyData.getRegistrationNumber());
					company.setCompanyNumber(companyData.getCompanyNumber());
					company.setBranchOfficeNumber(companyData.getBranchOfficeNumber());
					company.setPrimaryBusiness(companyData.getPrimaryBusiness());
					if (companyData.getOtherBusiness() != null) {
						company.setOtherBusiness(StringUtils.join(companyData.getOtherBusiness(), "|"));
					}
					companyRepository.save(company);

					log.debug("New company [{}] created", company.getId());

					importCompanyData(company, companyData);
				} else {
					user = company.getUser();
					user.setLastLoginDate(new DateTime());
					userRepository.save(user);

					importCompanyData(company, companyData);
				}

				resource.setEmail(user.getEmail());
				resource.setPassword(user.getPassword());

				return new ResponseEntity<>(resource, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ExternalCompanyResource getCompanyData(String matbr) throws Exception {
		String companyNumber = null;
		String branchOfficeNumber = null;

		if (matbr.contains("-")) {
			String[] array = matbr.split("\\-");
			companyNumber = array[0];
			branchOfficeNumber = array[1];
		} else {
			companyNumber = matbr;
			branchOfficeNumber = "";
		}

		log.debug("Getting company data from external source for company number [{}] and branch office number [{}]...", companyNumber, branchOfficeNumber);
		ResponseEntity<ExternalCompanyResource> responseEntity = restTemplate.exchange(baseProperties.getZabaApiGetByCompanyNumber(),
				HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), ExternalCompanyResource.class, getUriVariables(companyNumber, branchOfficeNumber));

		return responseEntity.getBody();
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private Map<String, String> getUriVariables(String companyNumber, String branchOfficeNumber) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("companyNumber", companyNumber);
		uriVariables.put("branchOfficeNumber", branchOfficeNumber);
		return uriVariables;
	}

	private void importCompanyData(Company company, ExternalCompanyResource companyData) {
		log.debug("Initiating import of company data...");

		importLegalTypeNumber(company, companyData);
		importZipCode(company, companyData);
		importFoundingDate(company, companyData);
		importBankruptcyProcedure(company, companyData);
		importBlocked5Days(company, companyData);
		importBlocked20Days(company, companyData);
		importLastYearIncome(company, companyData);
		importNumberOfEmployees(company, companyData);
		importProfitBeforeTax(company, companyData);
		importProfitOrLoss(company, companyData);
		importCapitalTotalLiabilities(company, companyData);
	}

	private void importLegalTypeNumber(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing legal type number [{}] for company [{}]", companyData.getLegalTypeNumber(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getLegalTypeNumberMapping(companyData.getLegalTypeNumber());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Legal type number successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing legal type number failed", e);
		}
	}

	private void importZipCode(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing ZIP code [{}] for company [{}]", companyData.getZipCode(), company.getName());

			Item item = externalFlowMappingService.getZipCodeMapping();
			if (StringUtils.isNotBlank(companyData.getZipCode())) {
				ZabaMappingsLocation zabaMappingsLocation = zabaMappingsLocationRepository.findByZipCode(companyData.getZipCode()).get(0);
				if (zabaMappingsLocation != null) {
					Subdivision2 subdivision2 = subdivision2Repository.findByName(zabaMappingsLocation.getSubdivision2());
					if (subdivision2 != null) {
						CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
						if (companyItem == null) {
							companyItem = new CompanyItem();
						}
						companyItem.setCompany(company);
						companyItem.setItem(item);
						companyItem.setValue(subdivision2.getId().toString());
						companyItemRepository.save(companyItem);

						log.debug("ZIP code successfully imported - company item [id: {}, value: {}]", companyItem.getId(), subdivision2.getName());
					}
				}
			}
		} catch (Exception e) {
			log.error("Importing ZIP code failed", e);
		}
	}

	private void importFoundingDate(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing founding date [{}] for company [{}]", companyData.getFoundingDate(), company.getName());

			Item item = externalFlowMappingService.getFoundingDateMapping();
			if (companyData.getFoundingDate() != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(item);
				companyItem.setValue(dateFormat.format(companyData.getFoundingDate()));
				companyItemRepository.save(companyItem);

				log.debug("Founding date successfully imported - company item [id: {}, value: {}]", companyItem.getId(), companyItem.getValue());
			}
		} catch (Exception e) {
			log.error("Importing founding date failed", e);
		}
	}

	private void importBankruptcyProcedure(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing bankruptcy procedure [{}] for company [{}]", companyData.getAnyBankruptcyProcedure(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getBankruptcyProcedureMapping(companyData.getAnyBankruptcyProcedure());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Bankruptcy procedure successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing bankruptcy procedure failed", e);
		}
	}

	private void importBlocked5Days(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing blocked 5 days [{}] for company [{}]", companyData.getIsBlockedFor5Days(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getBlocked5DaysMapping(companyData.getIsBlockedFor5Days());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Blocked 5 days successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing blocked 5 days failed", e);
		}
	}

	private void importBlocked20Days(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing blocked 20 days [{}] for company [{}]", companyData.getIsBlockedFor20Days(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getBlocked20DaysMapping(companyData.getIsBlockedFor20Days());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Blocked 20 days successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing blocked 20 days failed", e);
		}
	}

	private void importLastYearIncome(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing last year income [{}] for company [{}]", companyData.getLastYearIncome(), company.getName());

			Item item = externalFlowMappingService.getLastYearIncomeMapping();
			if (companyData.getLastYearIncome() != null) {

				Map<ItemOption, DoubleRange> ranges = new HashMap<>();
				for (ItemOption itemOption : item.getOptions()) {
					String value = itemOption.getText();
					value = value.substring(0, value.indexOf(" ")).trim().replace(".", "");
					if (value.contains("-")) {
						String[] values = value.split("\\-");
						ranges.put(itemOption, new DoubleRange(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
					} else if (value.contains("<")) {
						value = value.substring(value.indexOf("<")+1);
						ranges.put(itemOption, new DoubleRange(Double.MIN_VALUE, Double.parseDouble(value)));
					} else if (value.contains(">")) {
						value = value.substring(value.indexOf(">")+1);
						ranges.put(itemOption, new DoubleRange(Double.parseDouble(value), Double.MAX_VALUE));
					}
				}

				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(item);

				ItemOption itemOption = null;
				Double value = Double.valueOf(companyData.getLastYearIncome());
				for (Entry<ItemOption, DoubleRange> range : ranges.entrySet()) {
					if (range.getValue().containsDouble(value)) {
						itemOption = range.getKey();
						companyItem.setValue(itemOption.getId().toString());
						break;
					}
				}

				companyItemRepository.save(companyItem);

				log.debug("Last year income successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing last year income failed", e);
		}
	}

	private void importProfitBeforeTax(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing profit before tax [{}] for company [{}]", companyData.getIsProfitBeforeTax(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getProfitBeforeTaxMapping(companyData.getIsProfitBeforeTax());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Profit before tax successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing profit before tax failed", e);
		}
	}

	private void importProfitOrLoss(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing profit or loss [{}] for company [{}]", companyData.getProfitOrLoss(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getProfitOrLossMapping(companyData.getProfitOrLoss());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Profit or loss successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing profit or loss failed", e);
		}
	}

	private void importCapitalTotalLiabilities(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing capital [{}] / total liabilities [{}] for company [{}]", companyData.getCapital(), companyData.getTotalLiabilities(), company.getName());

			ItemOption itemOption = externalFlowMappingService.getCapitalTotalLiabilities(companyData.getCapital(), companyData.getTotalLiabilities());
			if (itemOption != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, itemOption.getItem());
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(itemOption.getItem());
				companyItem.setValue(itemOption.getId().toString());
				companyItemRepository.save(companyItem);

				log.debug("Capital / total liabilities successfully imported - company item [id: {}, value: {}]", companyItem.getId(), itemOption.getText());
			}
		} catch (Exception e) {
			log.error("Importing capital / total liabilities failed", e);
		}
	}

	private void importNumberOfEmployees(Company company, ExternalCompanyResource companyData) {
		try {
			log.debug("Importing number of employees [{}] for company [{}]", companyData.getNumberOfEmployeesAnnual(), company.getName());

			Item item = externalFlowMappingService.getNumberOfEmployeesMapping();
			if (companyData.getNumberOfEmployeesAnnual() != null) {
				CompanyItem companyItem = companyItemRepository.findByCompanyAndItem(company, item);
				if (companyItem == null) {
					companyItem = new CompanyItem();
				}
				companyItem.setCompany(company);
				companyItem.setItem(item);
				companyItem.setValue(companyData.getNumberOfEmployeesAnnual());
				companyItemRepository.save(companyItem);

				log.debug("Number of employees successfully imported - company item [id: {}, value: {}]", companyItem.getId(), companyItem.getValue());
			}
		} catch (Exception e) {
			log.error("Importing number of employees failed", e);
		}
	}

}
