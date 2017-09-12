package org.ff.rest.gfisync.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.gfisync.resource.GfiSyncErrorResource;
import org.ff.rest.gfisync.service.GfiSyncErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/gfisyncerrors" })
public class GfiSyncErrorController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private GfiSyncErrorService gfiSyncErrorService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public GfiSyncErrorResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return gfiSyncErrorService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
