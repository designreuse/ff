package org.ff.rest.useremail.controller;

import java.security.Principal;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.useremail.resource.UserEmailResource;
import org.ff.rest.useremail.service.UserEmailService;
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
