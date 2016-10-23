package org.ff.email.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.ff.controller.BaseController;
import org.ff.email.service.EmailService;
import org.ff.etm.EtmService;
import org.ff.resource.email.EmailResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/emails" })
public class EmailController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public EmailResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return emailService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

}
