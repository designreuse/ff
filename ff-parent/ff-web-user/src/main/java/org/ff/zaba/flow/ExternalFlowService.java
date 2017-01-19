package org.ff.zaba.flow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.password.PasswordService;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.CompanyItemRepository;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
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

	private RestTemplate restTemplate;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
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
					companyRepository.save(company);

					log.debug("New company [{}] created", company.getId());

					importCompanyData(company, companyData);
				} else {
					user = company.getUser();
					user.setLastLoginDate(new DateTime());
					userRepository.save(user);
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

	private void importCompanyData(Company company, ExternalCompanyResource companyData) throws Exception {
		log.debug("Initiating import of company data...");

		importLegalTypeNumber(company, companyData);
	}

	private void importLegalTypeNumber(Company company, ExternalCompanyResource companyData) throws Exception {
		ItemOption itemOption = externalFlowMappingService.getLegalTypeNumberMapping(companyData.getLegalTypeNumber());
		if (itemOption != null) {
			CompanyItem companyItem = new CompanyItem();
			companyItem.setCompany(company);
			companyItem.setItem(itemOption.getItem());
			companyItem.setValue(itemOption.getId().toString());
			companyItemRepository.save(companyItem);
		}
	}

}
