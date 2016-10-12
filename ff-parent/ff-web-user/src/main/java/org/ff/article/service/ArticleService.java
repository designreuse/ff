package org.ff.article.service;

import java.util.List;

import org.ff.jpa.domain.Article;
import org.ff.jpa.domain.Article.ArticleStatus;
import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.repository.ArticleRepository;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.resource.article.ArticleResource;
import org.ff.resource.article.ArticleResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleService {

	@Autowired
	private ArticleRepository repository;

	@Autowired
	private ArticleResourceAssembler resourceAssembler;

	@Autowired
	private ImpressionRepository impressionRepository;

	@Transactional(readOnly = true)
	public List<ArticleResource> findAll() {
		log.debug("Finding articles...");
		return resourceAssembler.toResources(repository.findByStatusOrderByLastModifiedDateDesc(ArticleStatus.ACTIVE), false);
	}

	@Transactional
	public ArticleResource find(Integer id) {
		log.debug("Finding article [{}]...", id);

		Article entity = repository.findOne(id);
		if (entity == null) {
			throw new RuntimeException(String.format("Article [%s] not found", id));
		}

		Impression impression = new Impression();
		impression.setEntityType(EntityType.ARTICLE);
		impression.setEntityId(entity.getId());
		impressionRepository.save(impression);

		return resourceAssembler.toResource(entity, false);
	}

}
