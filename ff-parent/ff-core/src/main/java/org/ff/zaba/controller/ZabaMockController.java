package org.ff.zaba.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ff.zaba.resource.ZabaCompanyResource;
import org.ff.zaba.resource.ZabaContactFormRequestResource;
import org.ff.zaba.resource.ZabaContactFormResponseResource;
import org.ff.zaba.resource.ZabaOfficeResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/e/api/v1/zaba" })
public class ZabaMockController {

	/**
	 * Mock method that simulates ZaBa API end-point for retrieving company data.
	 * @param companyNumber
	 * @param branchOfficeNumber
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getByCompanyNumber")
	public ResponseEntity<?> getByCompanyNumber(@RequestParam String companyNumber, @RequestParam String branchOfficeNumber) {
		try {
			ZabaCompanyResource resource = new ZabaCompanyResource();
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
			resource.setVpoFirstName("Jack");
			resource.setVpoLastName("Russel");
			resource.setVpoEmail("jack.russel@gmail.com");

			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getOffices")
	public ResponseEntity<?> getOffices() {
		try {
			List<ZabaOfficeResource> resources = new ArrayList<>();

			resources.add(new ZabaOfficeResource(61, "Poslovnica", "Za građanstvo", "Trg Franje Tuđmana bb", "Sinj", "21230"));
			resources.add(new ZabaOfficeResource(44, "Poslovnica", "Za građanstvo", "Trg Franje Tuđmana 1", "Virovitica", "33000"));

			return new ResponseEntity<>(resources, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/submitContactForm")
	public ResponseEntity<?> submitContactForm(@RequestBody ZabaContactFormRequestResource requestResource) {
		try {
			log.debug("Request: {}", requestResource);

			ZabaContactFormResponseResource responseResource = new ZabaContactFormResponseResource(false, 1, "Pogrešan OIB poduzeća", null);

			return new ResponseEntity<>(responseResource, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
