package org.ff.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ff.controller.BaseController;
import org.ff.etm.EtmService;
import org.ff.jpa.domain.User;
import org.ff.jpa.envers.RevisionResource;
import org.ff.jpa.envers.RevisionService;
import org.ff.resource.user.UserResource;
import org.ff.uigrid.PageableResource;
import org.ff.uigrid.UiGridResource;
import org.ff.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/users", "/e/api/v1/users" })
public class UserController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private UserService userService;

	@Autowired
	private RevisionService revisionService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<UserResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return userService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public UserResource find(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return userService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public UserResource save(@AuthenticationPrincipal UserDetails user, @RequestBody UserResource resource, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".save");
		try {
			return userService.save(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/activate")
	public UserResource activate(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return userService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public UserResource deactivate(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return userService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(@AuthenticationPrincipal UserDetails user, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			userService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/revisions")
	public List<RevisionResource> getRevisions(@AuthenticationPrincipal @PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getRevisions");
		try {
			return revisionService.getRevisions(User.class, id);
		} finally {
			etmService.collect(point);
		}
	}

}
