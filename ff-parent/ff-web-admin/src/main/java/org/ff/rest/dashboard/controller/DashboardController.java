package org.ff.rest.dashboard.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.dashboard.resource.ChartResource;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.ff.rest.dashboard.service.DashboardService;
import org.ff.rest.dashboard.service.DashboardService.DashboardChartPeriod;
import org.ff.rest.dashboard.service.DashboardService.DashboardChartType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/dashboard" })
public class DashboardController extends BaseController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public DashboardResource getData() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getData");
		try {
			return dashboardService.getData();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/chart/{period}/{type}")
	public ChartResource getChartData(@PathVariable String period, @PathVariable String type) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getChartData");
		try {
			return dashboardService.getChartData(DashboardChartPeriod.valueOf(period.toUpperCase()), DashboardChartType.valueOf(type.toUpperCase()));
		} finally {
			etmService.collect(point);
		}
	}

}
