package org.ff.rest.settings.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.settings.resource.SettingsResource;
import org.ff.rest.settings.service.SettingsService;
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
@RequestMapping(value = { "/api/v1/settings" })
public class SettingsController extends BaseController {

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public SettingsResource find(@AuthenticationPrincipal UserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return settingsService.find(principal);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public SettingsResource save(@AuthenticationPrincipal UserDetails principal, @RequestBody SettingsResource resources) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return settingsService.save(principal, resources);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
			log.debug(".save finished in {} ms", point.getTransactionTime());
		}
	}

}
