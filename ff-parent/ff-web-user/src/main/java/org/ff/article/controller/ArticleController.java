package org.ff.article.controller;

import java.util.List;

import org.ff.article.resource.ArticleResource;
import org.ff.article.service.ArticleService;
import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/articles" })
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<ArticleResource> findAll() {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return articleService.findAll();
		} finally {
			etmService.collect(point);
			log.debug(".findAll finished in {} ms", point.getTransactionTime());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ArticleResource find(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return articleService.find(id);
		} finally {
			etmService.collect(point);
			log.debug(".find finished in {} ms", point.getTransactionTime());
		}
	}

}
