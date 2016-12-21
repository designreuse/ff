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

	@RequestMapping(method = RequestMethod.GET, value="/companiesByInvestments")
	public ResponseEntity<?> getCompaniesByInvestments() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getCompaniesByInvestments");
		try {
			return statisticsService.getCompaniesByInvestments();
		} finally {
			etmService.collect(point);
			log.debug("getCompaniesByInvestments() finished in {} ms", point.getTransactionTime());
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

	@RequestMapping(method = RequestMethod.GET, value="/companiesBySectors")
	public ResponseEntity<?> getCompaniesBySectors() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getCompaniesBySectors");
		try {
			return statisticsService.getCompaniesBySectors();
		} finally {
			etmService.collect(point);
			log.debug("getCompaniesBySectors() finished in {} ms", point.getTransactionTime());
		}
	}

}
