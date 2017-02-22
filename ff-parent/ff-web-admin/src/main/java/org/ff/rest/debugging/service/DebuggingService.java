package org.ff.rest.debugging.service;

import org.ff.common.algorithm.AlgorithmService;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.debugging.resource.DebuggingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DebuggingService {

	@Autowired
	private AlgorithmService algorithmService;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private UserRepository userRepository;

	public DebuggingResource debug(Integer userId, Integer tenderId) {
		DebuggingResource debug = new DebuggingResource();

		debug.setIsMatch(algorithmService.isMatch(userRepository.findOne(userId), tenderRepository.findOne(tenderId), debug));

		return debug;
	}

}
