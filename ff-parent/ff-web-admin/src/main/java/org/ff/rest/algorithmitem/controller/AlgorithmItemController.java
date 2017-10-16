package org.ff.rest.algorithmitem.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.algorithmitem.resource.AlgorithmItemResource;
import org.ff.rest.algorithmitem.service.AlgorithmItemService;
import org.ff.rest.algorithmitem.validation.AlgorithmItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/algorithmitems" })
public class AlgorithmItemController extends BaseController {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private AlgorithmItemService algorithmItemService;

	@Autowired
	private AlgorithmItemValidator algorithmItemValidator;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<AlgorithmItemResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return algorithmItemService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<AlgorithmItemResource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return algorithmItemService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public AlgorithmItemResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return algorithmItemService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public AlgorithmItemResource save(Principal principal, @RequestBody AlgorithmItemResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			algorithmItemValidator.validate(resource, localeResolver.resolveLocale(request));
			return algorithmItemService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/activate")
	public AlgorithmItemResource activate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return algorithmItemService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public AlgorithmItemResource deactivate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return algorithmItemService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/delete/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			algorithmItemService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/export")
	public List<AlgorithmItemResource> exportAlgorithmItems() {
		return algorithmItemService.exportAlgorithmItems();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/import")
	public Integer importAlgorithmItems(@RequestParam MultipartFile file) {
		try {
			Object obj = objectMapper.readValue(file.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, AlgorithmItemResource.class));
			return algorithmItemService.importAlgorithmItems((List<AlgorithmItemResource>) obj);
		} catch (Exception e) {
			throw new RuntimeException("Parsing of imported file failed!", e);
		}
	}

}
