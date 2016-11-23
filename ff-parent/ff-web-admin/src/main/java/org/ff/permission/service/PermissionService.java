package org.ff.permission.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.jpa.repository.PermissionRepository;
import org.ff.resource.permission.PermissionResource;
import org.ff.resource.permission.PermissionResourceAssembler;
import org.ff.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PermissionService extends BaseService {

	@Autowired
	private PermissionRepository repository;

	@Autowired
	private PermissionResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<PermissionResource> findAll() {
		log.debug("Finding Permissions...");

		List<PermissionResource> result = resourceAssembler.toResources(repository.findAll());
		Collections.sort(result, new Comparator<PermissionResource>() {
			@Override
			public int compare(PermissionResource o1, PermissionResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

}
