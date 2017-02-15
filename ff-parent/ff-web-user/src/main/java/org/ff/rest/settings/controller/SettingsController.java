package org.ff.rest.settings.controller;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.security.AppUserDetails;
import org.ff.rest.settings.resource.SettingsResource;
import org.ff.rest.settings.service.SettingsService;
import org.ff.rest.settings.validation.SettingsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/settings" })
public class SettingsController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private SettingsValidator settingsValidator;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public SettingsResource find(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return settingsService.find(principal);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public SettingsResource save(@AuthenticationPrincipal AppUserDetails principal, @RequestBody SettingsResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			settingsValidator.validate(resource, localeResolver.resolveLocale(request));
			return settingsService.save(principal, resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
			log.debug(".save finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/deactivate")
	public SettingsResource deactivate(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return settingsService.deactivate(principal);
		} finally {
			etmService.collect(point);
			log.debug(".deactivate finished in {} ms", point.getTransactionTime());
		}
	}

}
