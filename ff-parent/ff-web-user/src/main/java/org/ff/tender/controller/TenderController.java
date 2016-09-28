package org.ff.tender.controller;

import java.util.List;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.resource.tender.TenderResource;
import org.ff.tender.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/tenders" })
public class TenderController extends BaseController {

	@Autowired
	private TenderService tenderService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<TenderResource> findAll(@AuthenticationPrincipal UserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return tenderService.findAll(principal);
		} finally {
			etmService.collect(point);
			log.debug(".findAll finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public TenderResource find(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return tenderService.find(id);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

}
