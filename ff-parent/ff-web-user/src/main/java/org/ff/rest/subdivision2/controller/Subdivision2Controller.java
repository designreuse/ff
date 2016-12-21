package org.ff.rest.subdivision2.controller;

import java.security.Principal;
import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.subdivision2.resource.Subdivision2Resource;
import org.ff.rest.subdivision2.service.Subdivision2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/subdivisions2" })
public class Subdivision2Controller extends BaseController {

	@Autowired
	private Subdivision2Service subdivision2Service;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Subdivision2Resource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return subdivision2Service.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
