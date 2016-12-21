package org.ff.rest.investment.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.investment.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/investments" })
public class InvestmentController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<InvestmentResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return investmentService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<InvestmentResource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return investmentService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public InvestmentResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return investmentService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public InvestmentResource save(Principal principal, @RequestBody InvestmentResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return investmentService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/activate")
	public InvestmentResource activate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return investmentService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public InvestmentResource deactivate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return investmentService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			investmentService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
