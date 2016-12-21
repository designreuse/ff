package org.ff.rest.impression.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.rest.impression.resource.ImpressionStatisticsResource;
import org.ff.rest.impression.service.ImpressionService;
import org.ff.rest.impression.service.ImpressionService.StatisticsPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/impressions" })
public class ImpressionController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private ImpressionService impressionService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/statistics/periods")
	public List<String> getStatisticsPeriods(Principal principal) {
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
	public ImpressionStatisticsResource getImpressionStatistics(Principal principal, @PathVariable String entity, @PathVariable Integer entityId, @PathVariable String statisticsPeriod, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getImpressionStatistics");
		try {
			return impressionService.getImpressionStatistics(EntityType.valueOf(entity), entityId, StatisticsPeriod.valueOf(statisticsPeriod), localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
