package org.ff.rest.dashboard.controller;

import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.security.AppUserDetails;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.ff.rest.dashboard.service.DashboardService;
import org.ff.rest.tender.resource.TenderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public DashboardResource getData(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getData");
		try {
			return dashboardService.getData(principal);
		} finally {
			etmService.collect(point);
		}
	}

	@GetMapping(value = "/getChartDetails")
	public List<TenderResource> getChartDetails(@RequestParam String period) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getChartDetails");
		try {
			return dashboardService.getChartDetails(period);
		} finally {
			etmService.collect(point);
		}
	}

}
