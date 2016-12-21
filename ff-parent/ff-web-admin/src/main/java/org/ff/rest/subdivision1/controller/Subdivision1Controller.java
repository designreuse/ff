package org.ff.rest.subdivision1.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.subdivision1.resource.Subdivision1Resource;
import org.ff.rest.subdivision1.service.Subdivision1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/subdivisions1" })
public class Subdivision1Controller extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private Subdivision1Service subdivision1Service;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<Subdivision1Resource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return subdivision1Service.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Subdivision1Resource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return subdivision1Service.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public Subdivision1Resource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return subdivision1Service.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public Subdivision1Resource save(Principal principal, @RequestBody Subdivision1Resource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return subdivision1Service.save(resource);
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
			subdivision1Service.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
