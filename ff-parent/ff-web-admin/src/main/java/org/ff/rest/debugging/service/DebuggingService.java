package org.ff.rest.debugging.service;

import org.ff.common.algorithm.AlgorithmService;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.ProjectRepository;
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

	@Autowired
	private ProjectRepository projectRepository;

	public DebuggingResource debug(Integer userId, Integer tenderId) {
		DebuggingResource debug = new DebuggingResource();

		User user = userRepository.findOne(userId);

		debug.setIsMatch(algorithmService.isMatch(user, user.getCompany(), projectRepository.findByCompany(user.getCompany()), tenderRepository.findOne(tenderId), debug));

		return debug;
	}

}
