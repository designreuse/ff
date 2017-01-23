package org.ff.rest.dashboard.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.ff.rest.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/dashboard" })
public class DashboardController extends BaseController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private EtmService etmService;

	@GetMapping
	public DashboardResource getData() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getData");
		try {
			return dashboardService.getData();
		} finally {
			etmService.collect(point);
		}
	}

}
