package org.ff.city.controller;

import java.util.List;

import org.ff.city.service.CityService;
import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.resource.city.CityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/cities", "/e/api/v1/cities" })
public class CityController extends BaseController {

	@Autowired
	private CityService cityService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<CityResource> findAll() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return cityService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
