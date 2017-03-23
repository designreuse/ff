package org.ff.rest.contact.controller;

import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.common.resource.KeyValueResource;
import org.ff.rest.contact.resource.ContactResource;
import org.ff.rest.contact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/api/v1/contact" })
public class ContactController extends BaseController {

	@Autowired
	private ContactService service;

	@RequestMapping(method = RequestMethod.GET, value="/locations")
	public List<KeyValueResource> getLocations() {
		return service.getLocations();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ContactResource get(@AuthenticationPrincipal UserDetails principal) {
		return service.get(principal);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void set(@RequestBody ContactResource resource) {
		service.set(resource);
	}

}
