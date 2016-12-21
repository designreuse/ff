package org.ff.rest.activity.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ff.base.service.BaseService;
import org.ff.jpa.repository.ActivityRepository;
import org.ff.rest.activity.resource.ActivityResource;
import org.ff.rest.activity.resource.ActivityResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityService extends BaseService {

	@Autowired
	private ActivityRepository repository;

	@Autowired
	private ActivityResourceAssembler resourceAssembler;

	@Autowired
	private Collator collator;

	@Transactional(readOnly = true)
	public List<ActivityResource> findAll() {
		List<ActivityResource> result = resourceAssembler.toResources(repository.findAll(), false);

		Collections.sort(result, new Comparator<ActivityResource>() {
			@Override
			public int compare(ActivityResource o1, ActivityResource o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		return result;
	}

}
