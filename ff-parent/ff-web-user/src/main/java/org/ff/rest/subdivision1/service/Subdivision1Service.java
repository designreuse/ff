package org.ff.rest.subdivision1.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.base.service.BaseService;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.rest.subdivision1.resource.Subdivision1Resource;
import org.ff.rest.subdivision1.resource.Subdivision1ResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Subdivision1Service extends BaseService {

	@Autowired
	private Subdivision1Repository repository;

	@Autowired
	private Subdivision1ResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Cacheable(value = "subdivisions1")
	@Transactional(readOnly = true)
	public List<Subdivision1Resource> findAll() {
		List<Subdivision1Resource> result = resourceAssembler.toResources(repository.findAll(), false);

		Collections.sort(result, new Comparator<Subdivision1Resource>() {
			@Override
			public int compare(Subdivision1Resource o1, Subdivision1Resource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

}
