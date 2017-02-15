package org.ff.rest.company.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.security.AppUserDetails;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/company" })
public class CompanyController extends BaseController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public CompanyResource find(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return companyService.find(principal);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public CompanyResource save(@AuthenticationPrincipal AppUserDetails principal, @RequestBody CompanyResource resource) {
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

	@RequestMapping(method = RequestMethod.GET, value = "/profileCompleteness/{all}")
	public Double profileCompleteness(@AuthenticationPrincipal AppUserDetails principal, @PathVariable Boolean all) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".profileCompleteness");
		try {
			return companyService.profileCompleteness(principal, all);
		} finally {
			etmService.collect(point);
			log.debug(".profileCompleteness finished in {} ms", point.getTransactionTime());
		}
	}

}
