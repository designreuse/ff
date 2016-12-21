package org.ff.rest.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/api/v1/users" })
public class UserController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private UserService userService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public ResponseEntity<?> register(@RequestBody UserResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".register");
		try {
			return userService.register(resource, localeResolver.resolveLocale(request));
		} catch (RuntimeException e) {
			log.error("Registering user failed", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody UserResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".resetPassword");
		try {
			return userService.resetPassword(resource, localeResolver.resolveLocale(request));
		} catch (RuntimeException e) {
			log.error("Reset password failed", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public void confirmRegistration(@RequestParam String registrationCode, HttpServletResponse response) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".confirmRegistration");
		try {
			userService.confirmRegistration(registrationCode, response);
		} finally {
			etmService.collect(point);
		}
	}

}
