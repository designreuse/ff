package org.ff.rest.contact.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.common.uigrid.PageableResource;
import org.ff.common.uigrid.UiGridResource;
import org.ff.rest.contact.resource.ContactResource;
import org.ff.rest.contact.service.ContactExportService;
import org.ff.rest.contact.service.ContactService;
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
@RequestMapping(value = { "/api/v1/contacts" })
public class ContactController extends BaseController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private ContactService contactService;

	@Autowired
	private ContactExportService contactExportService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public PageableResource<ContactResource> getPage(Principal principal, @RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".getPage");
		try {
			return contactService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ContactResource find(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return contactService.find(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/delete/{id}")
	public void delete(Principal principal, @PathVariable Integer id, HttpServletRequest request) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".delete");
		try {
			contactService.delete(id, localeResolver.resolveLocale(request));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/export/pdf", produces = "application/pdf")
	public ResponseEntity<byte[]> exportPdf(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".exportPdf");
		try {
			File file = contactExportService.exportPdf(id);
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
