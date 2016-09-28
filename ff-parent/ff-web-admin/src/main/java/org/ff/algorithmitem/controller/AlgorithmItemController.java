package org.ff.algorithmitem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.algorithmitem.service.AlgorithmItemService;
import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.resource.algorithmitem.AlgorithmItemResource;
import org.ff.uigrid.PageableResource;
import org.ff.uigrid.UiGridResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/algorithmitems", "/e/api/v1/algorithmitems" })
public class AlgorithmItemController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private AlgorithmItemService algorithmItemService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<AlgorithmItemResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return algorithmItemService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<AlgorithmItemResource> findAll() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return algorithmItemService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public AlgorithmItemResource find(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return algorithmItemService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public AlgorithmItemResource save(@AuthenticationPrincipal UserDetails user, @RequestBody AlgorithmItemResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return algorithmItemService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/activate")
	public AlgorithmItemResource activate(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return algorithmItemService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public AlgorithmItemResource deactivate(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return algorithmItemService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			algorithmItemService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
