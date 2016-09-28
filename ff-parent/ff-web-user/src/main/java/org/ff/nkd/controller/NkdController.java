package org.ff.nkd.controller;

import java.util.List;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.nkd.service.NkdService;
import org.ff.resource.nkd.NkdResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/nkds", "/e/api/v1/nkds" })
public class NkdController extends BaseController {

	@Autowired
	private NkdService nkdService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<NkdResource> findAll() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return nkdService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
