package org.ff.rest.gfisync.service;

import java.util.Locale;

import org.ff.base.service.BaseService;
import org.ff.jpa.domain.GfiSyncError;
import org.ff.jpa.repository.GfiSyncErrorRepository;
import org.ff.rest.gfisync.resource.GfiSyncErrorResource;
import org.ff.rest.gfisync.resource.GfiSyncErrorResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GfiSyncErrorService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private GfiSyncErrorRepository repository;

	@Autowired
	private GfiSyncErrorResourceAssembler resourceAssembler;

	@Transactional(readOnly = true)
	public GfiSyncErrorResource find(Integer id, Locale locale) {
		log.debug("Finding GfiSyncError [{}]...", id);

		GfiSyncError entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(messageSource.getMessage("exception.resourceNotFound",
					new Object[] { messageSource.getMessage("resource.gfiSyncError", null, locale), id }, locale));
		}

		return resourceAssembler.toResource(entity, false);
	}

}
