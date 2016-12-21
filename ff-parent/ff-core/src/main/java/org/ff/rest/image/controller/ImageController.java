package org.ff.rest.image.controller;

import org.ff.base.controller.BaseController;
import org.ff.common.etm.EtmService;
import org.ff.rest.image.resource.ImageResource;
import org.ff.rest.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;

@RestController
@RequestMapping(value = { "/api/v1/images" })
public class ImageController extends BaseController {

	@Autowired
	private ImageService imageService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ImageResource find(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint(getClass().getSimpleName() + ".find");
		try {
			return imageService.find(id);
		} finally {
			etmService.collect(point);
		}
	}

}
