package org.ff.rest.tender.controller;

import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.security.AppUserDetails;
import org.ff.rest.tender.resource.DemoResource;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.tender.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	public List<TenderResource> findAll(@AuthenticationPrincipal AppUserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return tenderService.findAll(principal);
		} finally {
			etmService.collect(point);
			log.debug(".findAll finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/demo")
	public List<TenderResource> findAllDemo(@AuthenticationPrincipal AppUserDetails principal, @RequestBody DemoResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAllDemo");
		try {
			return tenderService.findAllDemo(principal, resource);
		} finally {
			etmService.collect(point);
			log.debug(".findAllDemo finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public TenderResource find(@AuthenticationPrincipal AppUserDetails principal, @PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return tenderService.find(principal, id);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

}
