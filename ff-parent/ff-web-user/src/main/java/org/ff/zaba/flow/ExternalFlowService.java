package org.ff.zaba.flow;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ff.common.password.PasswordService;
import org.ff.jpa.domain.BusinessRelationshipManager;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.BusinessRelationshipManagerRepository;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.user.resource.UserResource;
import org.ff.zaba.resource.ZabaCompanyResource;
import org.ff.zaba.service.ZabaApiService;
import org.ff.zaba.service.ZabaUpdateService;
import org.ff.zaba.session.SessionClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalFlowService {

	@Autowired
	private SessionClient sessionClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private PasswordService passwordGeneratorService;

	@Autowired
	private BusinessRelationshipManagerRepository businessRelationshipManagerRepository;

	@Autowired
	private ZabaApiService zabaApiService;

	@Autowired
	private ZabaUpdateService zabaUpdateService;

	public ResponseEntity<UserResource> authorize(String authId) {
		try {
			log.debug("Initiating external flow for authId [{}]...", authId);

			UserResource resource = new UserResource();

			// get external user authorization data from SESSION TRANSFER SERVICE
			Map<String, String> data = sessionClient.checkAuthId(authId);

			if (data.containsKey("matbr")) {
				// get company data from external source (e.g. from ZaBa) via ReST API
				ZabaCompanyResource companyData = zabaApiService.getCompanyData(data.get("matbr"));

				log.debug("Company data: {}", companyData);

				// process BRM data
				BusinessRelationshipManager businessRelationshipManager = processVpoData(companyData);

				// check if company is already registered in FundFinder
				Company company = companyRepository.findByCode(companyData.getOibNumber());
				User user = null;
				String password = passwordGeneratorService.generate();

				if (company == null) {
					log.debug("Creating new user and company...");

					user = new User();
					user.setStatus(UserStatus.ACTIVE);
					user.setFirstName(data.get("ime"));
					user.setLastName(data.get("prezime"));

					if (data.containsKey("email") && StringUtils.isNotBlank(data.get("email"))) {
						if (userRepository.findByEmail(data.get("email")) != null) {
							log.warn("User with e-mail [{}] is already registered", data.get("email"));
							return new ResponseEntity<>(HttpStatus.CONFLICT);
						}
						user.setEmail(data.get("email"));
					}

					user.setPassword(PasswordService.encodePassword(password));
					user.setLastLoginDate(new DateTime());
					user.setDemoUser(Boolean.FALSE);
					user.setRegistrationType(UserRegistrationType.EXTERNAL);
					if (businessRelationshipManager != null) {
						user.setBusinessRelationshipManager(businessRelationshipManager);
					}
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
					company.setSyncData(Boolean.FALSE);
					company.setHideSyncDataWarning(Boolean.FALSE);
					companyRepository.save(company);

					log.debug("New company [{}] created", company.getId());

					zabaUpdateService.updateCompanyData(company, companyData);
				} else {
					log.debug("Existing company detected...");

					user = company.getUser();
					user.setFirstName(data.get("ime"));
					user.setLastName(data.get("prezime"));
					if (data.containsKey("email") && StringUtils.isNotBlank(data.get("email"))) {
						User userTmp = userRepository.findByEmail(data.get("email"));
						if (userTmp != null && !userTmp.getId().equals(user.getId())) {
							log.warn("User with e-mail [{}] already exists; existing e-mail won't be changed", data.get("email"));
						} else {
							user.setEmail(data.get("email"));
						}
					}
					user.setLastLoginDate(new DateTime());
					if (businessRelationshipManager != null) {
						user.setBusinessRelationshipManager(businessRelationshipManager);
					}
					userRepository.save(user);

					zabaUpdateService.updateCompanyData(company, companyData);
				}

				resource.setId(user.getId());
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

	private BusinessRelationshipManager processVpoData(ZabaCompanyResource companyData) throws Exception {
		BusinessRelationshipManager businessRelationshipManager = null;

		if (StringUtils.isNotBlank(companyData.getVpoEmail())) {
			businessRelationshipManager = businessRelationshipManagerRepository.findByEmail(companyData.getVpoEmail());

			if (businessRelationshipManager == null) {
				// create
				businessRelationshipManager = new BusinessRelationshipManager();
				businessRelationshipManager.setFirstName(companyData.getVpoFirstName());
				businessRelationshipManager.setLastName(companyData.getVpoLastName());
				businessRelationshipManager.setEmail(companyData.getVpoEmail());
			} else {
				// update
				if (StringUtils.isNotBlank(companyData.getVpoFirstName())) {
					businessRelationshipManager.setFirstName(companyData.getVpoFirstName());
				}
				if (StringUtils.isNotBlank(companyData.getVpoLastName())) {
					businessRelationshipManager.setLastName(companyData.getVpoLastName());
				}
			}

			businessRelationshipManagerRepository.save(businessRelationshipManager);
		}

		return businessRelationshipManager;
	}

}
