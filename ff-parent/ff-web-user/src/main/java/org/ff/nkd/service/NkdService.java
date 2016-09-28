package org.ff.nkd.service;

import java.util.List;

import org.ff.jpa.repository.NkdRepository;
import org.ff.resource.nkd.NkdResource;
import org.ff.resource.nkd.NkdResourceAssembler;
import org.ff.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NkdService extends BaseService {

	@Autowired
	private NkdRepository repository;

	@Autowired
	private NkdResourceAssembler resourceAssembler;

	@Cacheable("nkds")
	@Transactional(readOnly = true)
	public List<NkdResource> findAll() {
		log.debug("Finding NKDs...");
		return resourceAssembler.toResources(repository.findAll(), false);
	}

}
