package org.ff.user.controller;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.resource.user.UserResource;
import org.ff.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/users" })
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public ResponseEntity<?> register(@RequestBody UserResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".register");
		try {
			return userService.register(resource);
		} catch (RuntimeException e) {
			log.error("Registering user failed", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			etmService.collect(point);
		}
	}

}
