package org.ff.rest.configparam.controller;

import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.configparam.resource.ConfigParamResource;
import org.ff.rest.configparam.service.ConfigParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/configparams" })
public class ConfigParamController extends BaseController {

	@Autowired
	private ConfigParamService configParamService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<ConfigParamResource> findAll() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return configParamService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public List<ConfigParamResource> save(@RequestBody List<ConfigParamResource> resources) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return configParamService.save(resources);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

}
