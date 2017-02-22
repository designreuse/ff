package org.ff.rest.debugging.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.debugging.resource.DebuggingResource;
import org.ff.rest.debugging.service.DebuggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/debugging" })
public class DebuggingController extends BaseController {

	@Autowired
	private DebuggingService debuggingService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}/{tenderId}")
	public DebuggingResource debug(@PathVariable Integer userId, @PathVariable Integer tenderId) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".debug");
		try {
			return debuggingService.debug(userId, tenderId);
		} finally {
			etmService.collect(point);
		}
	}

}
