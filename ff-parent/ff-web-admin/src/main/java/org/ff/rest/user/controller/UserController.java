package org.ff.rest.user.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.email.resource.SendEmailResource;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.service.UserExportService;
import org.ff.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.google.common.io.Files;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/users" })
public class UserController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private UserService userService;

	@Autowired
	private UserExportService userExportService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<UserResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return userService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public UserResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return userService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<UserResource> findAll(Principal principal) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".findAll");
		try {
			return userService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public UserResource save(Principal principal, @RequestBody UserResource resource, HttpServletRequest request) {
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
	public UserResource activate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".activate");
		try {
			return userService.activate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/deactivate")
	public UserResource deactivate(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".deactivate");
		try {
			return userService.deactivate(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			userService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/email")
	public void sendEmail(Principal principal, @RequestBody SendEmailResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".sendEmail");
		try {
			userService.sendEmail(resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/{id}/brm")
	public void setBusinessRelationshipManager(Principal principal, @PathVariable Integer id, @RequestBody UserResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".setBusinessRelationshipManager");
		try {
			userService.setBusinessRelationshipManager(id, resource);
		} catch (RuntimeException e) {
			throw processException(e);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/export/pdf", produces = "application/pdf")
	public ResponseEntity<byte[]> exportPdf(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".exportPdf");
		try {
			File file = userExportService.exportPdf(id);
			byte[] pdfContents = Files.toByteArray(file);
			file.delete();

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", "inline;filename=" + file.getName());
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(pdfContents, headers, HttpStatus.OK);
			return responseEntity;
		} finally {
			etmService.collect(point);
		}
	}

}
