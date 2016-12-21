package org.ff.rest.permission.controller;

import java.security.Principal;
import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.permission.resource.PermissionResource;
import org.ff.rest.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/permissions" })
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<PermissionResource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return permissionService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
