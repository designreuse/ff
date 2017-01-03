package org.ff.rest.companyinvestment.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.companyinvestment.resource.CompanyInvestmentResource;
import org.ff.rest.companyinvestment.service.CompanyInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/companyinvestments" })
public class CompanyInvestmentController extends BaseController {

	@Autowired
	private CompanyInvestmentService companyInvestmentService;

	@Autowired
	private EtmService etmService;


	@RequestMapping(method = RequestMethod.GET)
	public List<CompanyInvestmentResource> findAll(@AuthenticationPrincipal UserDetails principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return companyInvestmentService.findAll(principal);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public CompanyInvestmentResource find(@AuthenticationPrincipal UserDetails principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return companyInvestmentService.find(principal, id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public CompanyInvestmentResource save(@AuthenticationPrincipal UserDetails principal, @RequestBody CompanyInvestmentResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return companyInvestmentService.save(principal, resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(@AuthenticationPrincipal UserDetails principal, @PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			companyInvestmentService.delete(principal, id);
		} finally {
			etmService.collect(point);
		}
	}

}
