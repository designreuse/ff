package org.ff.permission.controller;

import java.security.Principal;
import java.util.List;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.permission.service.PermissionService;
import org.ff.resource.permission.PermissionResource;
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
