package org.ff.impression.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.impression.service.ImpressionService;
import org.ff.impression.service.ImpressionService.StatisticsPeriod;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.resource.impression.ImpressionStatisticsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/impressions", "/e/api/v1/impressions" })
public class ImpressionController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private ImpressionService impressionService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/statistics/periods")
	public List<String> getStatisticsPeriods() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getStatisticsPeriods");
		try {
			List<String> result = new ArrayList<>();
			for (StatisticsPeriod period : StatisticsPeriod.values()) {
				result.add(period.toString());
			}
			return result;
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/statistics/{entity}/{entityId}/{statisticsPeriod}")
	public ImpressionStatisticsResource getImpressionStatistics(@PathVariable String entity, @PathVariable Integer entityId, @PathVariable String statisticsPeriod, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getImpressionStatistics");
		try {
			return impressionService.getImpressionStatistics(EntityType.valueOf(entity), entityId, StatisticsPeriod.valueOf(statisticsPeriod), localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
