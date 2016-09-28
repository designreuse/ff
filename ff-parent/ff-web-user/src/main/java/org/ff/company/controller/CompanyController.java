package org.ff.company.controller;

import org.ff.company.service.CompanyService;
import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.resource.company.CompanyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/company", "/e/api/v1/company" })
public class CompanyController extends BaseController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public CompanyResource find(@AuthenticationPrincipal UserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return companyService.find(principal);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public CompanyResource save(@AuthenticationPrincipal UserDetails principal, @RequestBody CompanyResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return companyService.save(principal, resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
			log.debug(".save finished in {} ms", point.getTransactionTime());
		}
	}

}
