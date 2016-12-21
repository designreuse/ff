package org.ff.rest.counters.controller;

import java.security.Principal;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.counters.resource.CountersResource;
import org.ff.rest.counters.service.CountersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/counters" })
public class CountersController extends BaseController {

	@Autowired
	private CountersService counterService;

	@Autowired
	private EtmService etmService;

	@GetMapping
	public CountersResource findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return counterService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
