package org.ff.useremail.controller;

import java.security.Principal;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.resource.useremail.UserEmailResource;
import org.ff.uigrid.PageableResource;
import org.ff.uigrid.UiGridResource;
import org.ff.useremail.service.UserEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/useremails" })
public class UserEmailController extends BaseController {

	@Autowired
	private UserEmailService userEmailService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<UserEmailResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return userEmailService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

}
