package org.ff.zaba.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.user.resource.UserResource;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/e/api/v1/session" })
public class SessionController {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private SessionClient sessionClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	private RestTemplate restTemplate;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/authorize")
	public ResponseEntity<UserResource> authorize(@RequestParam String authId) {
		try {
			log.debug("Initiating external flow authorization for authId [{}]...", authId);

			UserResource resource = new UserResource();

			// get external user authorization data from SESSION TRANSFER SERVICE
			Map<String, String> data = sessionClient.checkAuthId(authId);
			if (data.containsKey("matbr")) {
				String companyNumber = null;
				String branchOfficeNumber = null;

				String matbr = data.get("matbr");
				if (matbr.contains("-")) {
					String[] array = matbr.split("\\-");
					companyNumber = array[0];
					branchOfficeNumber = array[1];
				} else {
					companyNumber = matbr;
					branchOfficeNumber = "";
				}

				// get company data from external source (e.g. from ZaBa) via ReST API
				log.debug("Getting company data from external source...");
				ResponseEntity<CompanyDataResource> responseEntity = restTemplate.exchange(baseProperties.getZabaApiGetByCompanyNumber(),
						HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), CompanyDataResource.class, getUriVariables(companyNumber, branchOfficeNumber));

				CompanyDataResource companyData = responseEntity.getBody();
				log.debug("Company data: {}", companyData);

				// check if company is already registered in FundFinder
				Company company = companyRepository.findByCode(companyData.getOibNumber());
				User user = null;

				if (company == null) {
					user = new User();
					user.setStatus(UserStatus.ACTIVE);
					user.setFirstName("John"); // TODO
					user.setLastName("Doe"); // TODO
					user.setEmail(companyData.getOibNumber() + "@fundfinder.hr"); // TODO
					user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword(user.getEmail(), null)); // TODO
					user.setLastLoginDate(new DateTime());
					user.setDemoUser(Boolean.FALSE);
					user.setRegistrationType(UserRegistrationType.EXTERNAL);
					userRepository.save(user);

					log.debug("New user [{}] created", user.getId());

					company = new Company();
					company.setUser(user);
					company.setName(companyData.getSubjectName());
					company.setCode(companyData.getOibNumber());
					companyRepository.save(company);

					log.debug("New company [{}] created", company.getId());
				} else {
					user = company.getUser();
					user.setLastLoginDate(new DateTime());
					userRepository.save(user);
				}

				resource.setEmail(user.getEmail());
				resource.setPassword("unknown");

				return new ResponseEntity<>(resource, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getByCompanyNumber")
	public ResponseEntity<?> getByCompanyNumber(@RequestParam String companyNumber, @RequestParam String branchOfficeNumber) {
		try {
			CompanyDataResource resource = new CompanyDataResource();
			resource.setSubjectName("VERBUM D.O.O.ZA GRAFIČKO OBLIKOVANJE,IZDAVAŠTVO I TRGOVINU");
			resource.setOibNumber("49355429927");
			resource.setRegistrationNumber("0000000078");
			resource.setCompanyNumber(companyNumber);
			resource.setBranchOfficeNumber(branchOfficeNumber);
			resource.setLegalTypeNumber("92");
			resource.setLegalType("D.O.O. (DRUŠT.S OGRANIČ.ODGOV)");
			resource.setZipCode("21000");
			resource.setRegistrationCounty("Splitsko-dalmatinska");
			resource.setFoundingDate(new Date());
			resource.setPrimaryBusiness("primaryBusiness");
			resource.setOtherBusiness(new ArrayList<String>());
			resource.setAnyBankruptcyProcedure("anyBankruptcyProcedure");
			resource.setIsBlockedFor5Days(Boolean.FALSE);
			resource.setIsBlockedFor20Days(Boolean.TRUE);
			resource.setLastYearIncome("11685632.00");
			resource.setIsProfitBeforeTax("625913.00");
			resource.setProfitOrLoss("472055.00");
			resource.setNumberOfEmployeesAnnual("numberOfEmployeesAnnual");
			resource.setCapital("4642015.00");
			resource.setTotalLiabilities("11476427.00");

			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
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

}
