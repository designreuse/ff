package org.ff.investment.controller;

import java.util.List;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.investment.resource.InvestmentResource;
import org.ff.investment.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/investments", "/e/api/v1/investments" })
public class InvestmentController extends BaseController {

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<InvestmentResource> findAll(@AuthenticationPrincipal UserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return investmentService.findAll(principal);
		} finally {
			etmService.collect(point);
			log.debug(".findAll finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public List<InvestmentResource> save(@AuthenticationPrincipal UserDetails principal, @RequestBody List<InvestmentResource> resources) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return investmentService.save(principal, resources);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
			log.debug(".save finished in {} ms", point.getTransactionTime());
		}
	}

}
