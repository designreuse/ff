package org.ff.zaba.flow;

import java.util.ArrayList;
import java.util.Date;

import org.ff.rest.user.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/e/api/v1/externalflow" })
public class ExternalFlowController {

	@Autowired
	private ExternalFlowService externalFlowService;

	@RequestMapping(method = RequestMethod.GET, value = "/authorize")
	public ResponseEntity<UserResource> authorize(@RequestParam String authId) {
		return externalFlowService.authorize(authId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getByCompanyNumber")
	public ResponseEntity<?> getByCompanyNumber(@RequestParam String companyNumber, @RequestParam String branchOfficeNumber) {
		try {
			ExternalCompanyResource resource = new ExternalCompanyResource();
			resource.setSubjectName("VERBUM D.O.O.ZA GRAFIČKO OBLIKOVANJE,IZDAVAŠTVO I TRGOVINU");
			resource.setOibNumber("49355429927");
			resource.setRegistrationNumber("0000000078");
			resource.setCompanyNumber(companyNumber);
			resource.setBranchOfficeNumber(branchOfficeNumber);
			resource.setLegalTypeNumber("52");
			resource.setLegalType("D.O.O. (DRUŠT.S OGRANIČ.ODGOV)");
			resource.setZipCode("21000");
			resource.setRegistrationCounty("Splitsko-dalmatinska");
			resource.setFoundingDate(new Date());
			resource.setPrimaryBusiness("primaryBusiness");
			resource.setOtherBusiness(new ArrayList<String>());
			resource.setAnyBankruptcyProcedure("L");
			resource.setIsBlockedFor5Days(Boolean.TRUE);
			resource.setIsBlockedFor20Days(Boolean.FALSE);
			resource.setLastYearIncome("11685632.00");
			resource.setIsProfitBeforeTax("625913.00");
			resource.setProfitOrLoss("-472055.00");
			resource.setNumberOfEmployeesAnnual("99");
			resource.setCapital("4642015.00");
			resource.setTotalLiabilities("11476427.00");

			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
