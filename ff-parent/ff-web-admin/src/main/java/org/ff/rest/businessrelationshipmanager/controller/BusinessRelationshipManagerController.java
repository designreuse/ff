package org.ff.rest.businessrelationshipmanager.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.businessrelationshipmanager.resource.BusinessRelationshipManagerResource;
import org.ff.rest.businessrelationshipmanager.service.BusinessRelationshipManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/businessrelationshipmanagers" })
public class BusinessRelationshipManagerController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private BusinessRelationshipManagerService businessRelationshipManagerService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<BusinessRelationshipManagerResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return businessRelationshipManagerService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<BusinessRelationshipManagerResource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return businessRelationshipManagerService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public BusinessRelationshipManagerResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return businessRelationshipManagerService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public BusinessRelationshipManagerResource save(Principal principal, @RequestBody BusinessRelationshipManagerResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return businessRelationshipManagerService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			businessRelationshipManagerService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
