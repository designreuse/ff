package org.ff.rest.company.controller;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.security.AppUserDetails;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.company.resource.ProfileCompletenessResource;
import org.ff.rest.company.service.CompanyService;
import org.ff.rest.company.validation.CompanyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/company" })
public class CompanyController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyValidator companyValidator;

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
	public CompanyResource save(@AuthenticationPrincipal AppUserDetails principal, @RequestBody CompanyResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			companyValidator.validate(resource, localeResolver.resolveLocale(request));
			return companyService.save(principal, resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
			log.debug(".save finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profileCompleteness")
	public ProfileCompletenessResource profileCompleteness(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".profileCompleteness");
		try {
			return companyService.profileCompleteness(principal);
		} finally {
			etmService.collect(point);
			log.debug(".profileCompleteness finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getHideSyncDataWarning")
	public Boolean getHideSyncDataWarning(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getHideSyncDataWarning");
		try {
			return companyService.getHideSyncDataWarning(principal);
		} finally {
			etmService.collect(point);
			log.debug(".getHideSyncDataWarning finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/showSyncDataWarningOrNot")
	public Boolean showSyncDataWarningOrNot(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".showSyncDataWarningOrNot");
		try {
			return companyService.showSyncDataWarningOrNot(principal);
		} finally {
			etmService.collect(point);
			log.debug(".showSyncDataWarningOrNot finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/syncData/{option}/{hideSyncDataWarning}")
	public void syncData(@AuthenticationPrincipal AppUserDetails principal, @PathVariable Integer option, @PathVariable Boolean hideSyncDataWarning) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".syncData");
		try {
			companyService.syncData(principal, option, hideSyncDataWarning);
		} finally {
			etmService.collect(point);
			log.debug(".syncData finished in {} ms", point.getTransactionTime());
		}
	}

	/** keep alive method */
	@RequestMapping(method = RequestMethod.GET, value = { "/ping" })
	public void ping() {
		// do nothing
	}

}
