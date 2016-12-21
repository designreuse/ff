package org.ff.rest.activity.controller;

import java.security.Principal;
import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.activity.resource.ActivityResource;
import org.ff.rest.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/activities" })
public class ActivityController extends BaseController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public List<ActivityResource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return activityService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

}
