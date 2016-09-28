package org.ff.item.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.item.service.ItemService;
import org.ff.item.validation.ItemValidator;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.envers.RevisionResource;
import org.ff.jpa.envers.RevisionService;
import org.ff.resource.item.ItemResource;
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
@RequestMapping(value = { "/api/v1/items", "/e/api/v1/items" })
public class ItemController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private ItemValidator itemValidator;

	@Autowired
	private ItemService itemService;

	@Autowired
	private RevisionService revisionService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<ItemResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return itemService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{entityType}")
	public List<ItemResource> findAll(@AuthenticationPrincipal UserDetails user, @PathVariable String entityType, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return itemService.findAll(ItemEntityType.valueOf(entityType));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{entityType}/{id}")
	public ItemResource find(@AuthenticationPrincipal UserDetails user, @PathVariable String entityType, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return itemService.find(entityType, id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ItemResource save(@AuthenticationPrincipal UserDetails user, @RequestBody ItemResource resource, HttpServletRequest request) {
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
	public ItemResource activate(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return itemService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public ItemResource deactivate(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return itemService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			itemService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/revisions")
	public List<RevisionResource> getRevisions(@AuthenticationPrincipal @PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getRevisions");
		try {
			return revisionService.getRevisions(Item.class, id);
		} finally {
			etmService.collect(point);
		}
	}

}
