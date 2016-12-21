package org.ff.rest.subdivision2.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.base.service.BaseService;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.rest.subdivision2.resource.Subdivision2Resource;
import org.ff.rest.subdivision2.resource.Subdivision2ResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Subdivision2Service extends BaseService {

	@Autowired
	private Subdivision2Repository repository;

	@Autowired
	private Subdivision2ResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Cacheable("subdivisions2")
	@Transactional(readOnly = true)
	public List<Subdivision2Resource> findAll() {
		List<Subdivision2Resource> result = resourceAssembler.toResources(repository.findAll(), false);
		Collections.sort(result, new Comparator<Subdivision2Resource>() {
			@Override
			public int compare(Subdivision2Resource o1, Subdivision2Resource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

}
