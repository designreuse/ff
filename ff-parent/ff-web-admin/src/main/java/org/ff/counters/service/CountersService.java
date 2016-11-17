package org.ff.counters.service;

import org.ff.counters.resource.CountersResource;
import org.ff.jpa.repository.ArticleRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.sse.resource.SseResource;
import org.ff.sse.resource.SseResource.SseType;
import org.ff.sse.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountersService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private SseService sseService;

	public CountersResource findAll() {
		CountersResource resource = new CountersResource();
		resource.setCntUsers(userRepository.countByDemoUser(Boolean.FALSE));
		resource.setCntTenders(tenderRepository.count());
		resource.setCntInvestments(investmentRepository.count());
		resource.setCntArticles(articleRepository.count());
		return resource;
	}

	public void sendEvent() {
		SseResource resource = new SseResource();
		resource.setType(SseType.COUNTERS_UPDATE);
		resource.setCounters(findAll());
		sseService.sendEvent(resource);
	}

}
