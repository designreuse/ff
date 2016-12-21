package org.ff.rest.subdivision1.controller;

import java.security.Principal;
import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.subdivision1.resource.Subdivision1Resource;
import org.ff.rest.subdivision1.service.Subdivision1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/subdivisions1" })
public class Subdivision1Controller extends BaseController {

	@Autowired
	private Subdivision1Service subdivision1Service;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Subdivision1Resource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return subdivision1Service.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
