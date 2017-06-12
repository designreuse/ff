package org.ff.zaba.flow;

import org.ff.rest.user.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/e/api/v1/externalflow" })
public class ExternalFlowController {

	@Autowired
	private ExternalFlowService externalFlowService;

	@RequestMapping(method = RequestMethod.GET, value = "/authorize")
	public ResponseEntity<UserResource> authorize(@RequestParam String authId) {
		return externalFlowService.authorize(authId);
	}

}
