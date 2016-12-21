package org.ff.rest.article.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.article.resource.ArticleResource;
import org.ff.rest.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/articles" })
public class ArticleController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<ArticleResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return articleService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ArticleResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return articleService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ArticleResource save(Principal principal, @RequestBody ArticleResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return articleService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/activate")
	public ArticleResource activate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return articleService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public ArticleResource deactivate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return articleService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			articleService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
