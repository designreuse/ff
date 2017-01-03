package org.ff.rest.image.service;

import org.ff.base.service.BaseService;
import org.ff.jpa.repository.ImageRepository;
import org.ff.rest.image.resource.ImageResource;
import org.ff.rest.image.resource.ImageResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService extends BaseService {

	@Autowired
	private ImageRepository repository;

	@Autowired
	private ImageResourceAssembler resourceAssembler;

	@Transactional(readOnly = true)
	public ImageResource find(Integer id) {
		log.debug("Finding image [{}]...", id);
		return resourceAssembler.toResource(repository.findOne(id), false);
	}

}
