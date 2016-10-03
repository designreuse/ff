package org.ff.sse.controller;

import org.ff.sse.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(value = { "/api/v1/sse" })
public class SseController {

	@Autowired
	private SseService sseService;

	@RequestMapping(method = RequestMethod.GET, value = "/emitter")
	public SseEmitter getEmitter() {
		return sseService.getEmitter();
	}

}
