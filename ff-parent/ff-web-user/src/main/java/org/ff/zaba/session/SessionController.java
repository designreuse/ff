package org.ff.zaba.session;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/e/api/v1/session" })
public class SessionController {

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

}
