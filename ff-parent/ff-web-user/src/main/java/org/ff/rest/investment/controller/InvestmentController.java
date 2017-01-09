package org.ff.rest.investment.controller;

import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.investment.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/investments", "/e/api/v1/investments" })
public class InvestmentController extends BaseController {

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<InvestmentResource> findAll() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return investmentService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
