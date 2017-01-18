package org.ff.rest.statistics.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/statistics" })
public class StatisticsController extends BaseController {

	@Autowired
	private StatisticsService statisticsService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/companiesByCounties")
	public ResponseEntity<?> getCompaniesByCounties() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getCompaniesByCounties");
		try {
			return statisticsService.getCompaniesByCounties();
		} finally {
			etmService.collect(point);
			log.debug("getCompaniesByCounties() finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/companiesByRevenues")
	public ResponseEntity<?> getCompaniesByRevenues() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getCompaniesByRevenues");
		try {
			return statisticsService.getCompaniesByRevenues();
		} finally {
			etmService.collect(point);
			log.debug("getCompaniesByRevenues() finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/investmentsByCounties")
	public ResponseEntity<?> getInvestmentsByCounties() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getInvestmentsByCounties");
		try {
			return statisticsService.getInvestmentsByCounties();
		} finally {
			etmService.collect(point);
			log.debug("getInvestmentsByCounties() finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/investmentsByActivities")
	public ResponseEntity<?> getInvestmentsByActivities() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getInvestmentsByActivities");
		try {
			return statisticsService.getInvestmentsByActivities();
		} finally {
			etmService.collect(point);
			log.debug("getInvestmentsByActivities() finished in {} ms", point.getTransactionTime());
		}
	}

}
