package org.ff.rest.item.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.service.ItemService;
import org.ff.rest.item.validation.ItemValidator;
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
@RequestMapping(value = { "/api/v1/items" })
public class ItemController extends BaseController {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private ItemValidator itemValidator;

	@Autowired
	private ItemService itemService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<ItemResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return itemService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{entityType}")
	public List<ItemResource> findAll(Principal principal, @PathVariable String entityType, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return itemService.findAll(ItemEntityType.valueOf(entityType));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{entityType}/{id}")
	public ItemResource find(Principal principal, @PathVariable String entityType, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return itemService.find(entityType, id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ItemResource save(Principal principal, @RequestBody ItemResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			itemValidator.validate(resource, localeResolver.resolveLocale(request));
			return itemService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/activate")
	public ItemResource activate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return itemService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public ItemResource deactivate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return itemService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			itemService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{entityType}/metatags")
	public List<String> getMetatags(Principal principal, @PathVariable String entityType) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getMetatags");
		try {
			return itemService.getMetatags(ItemEntityType.valueOf(entityType));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/export/{entityType}")
	public List<ItemResource> exportItems(@PathVariable String entityType) {
		return itemService.exportItems(ItemEntityType.valueOf(entityType.toUpperCase()));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/import")
	public Integer importItems(@RequestParam MultipartFile file) {
		try {
			Object obj = objectMapper.readValue(file.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, ItemResource.class));
			return itemService.importItems((List<ItemResource>) obj);
		} catch (Exception e) {
			throw new RuntimeException("Parsing of imported file failed!", e);
		}
	}

}
