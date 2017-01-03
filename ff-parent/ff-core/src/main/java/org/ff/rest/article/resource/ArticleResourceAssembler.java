package org.ff.rest.article.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.jpa.domain.Article;
import org.ff.jpa.domain.Image;
import org.ff.jpa.repository.ImageRepository;
import org.ff.rest.image.resource.ImageResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleResourceAssembler {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ImageResourceAssembler imageResourceAssembler;

	public ArticleResource toResource(Article entity, boolean light) {
		ArticleResource resource = new ArticleResource();
		resource.setId(entity.getId());
		resource.setStatus(entity.getStatus());
		resource.setName(entity.getName());
		if (!light) {
			resource.setText(entity.getText());
		}
		if (!light && entity.getImage() != null) {
			resource.setImage(imageResourceAssembler.toResource(entity.getImage(), false));
		}
		resource.setCreationDate(entity.getCreationDate().toDate());
		resource.setCreatedBy(entity.getCreatedBy());
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		resource.setLastModifiedBy(entity.getLastModifiedBy());
		return resource;
	}

	public List<ArticleResource> toResources(List<Article> entities, boolean light) {
		List<ArticleResource> resources = new ArrayList<>();
		for (Article entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	public Article createEntity(ArticleResource resource) {
		Article entity = new Article();
		entity.setStatus((resource.getStatus() != null) ? resource.getStatus() : Article.ArticleStatus.INACTIVE);
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		Image image = new Image();
		if (resource.getImage() != null) {
			image.setBase64(resource.getImage().getBase64());
		}
		entity.setImage(imageRepository.save(image));
		return entity;
	}

	public Article updateEntity(Article entity, ArticleResource resource) {
		entity.setStatus(resource.getStatus());
		entity.setName(resource.getName());
		entity.setText(resource.getText());
		if (resource.getImage() != null) {
			Image image = imageRepository.findOne(resource.getImage().getId());
			image.setBase64(resource.getImage().getBase64());
			imageRepository.save(image);
			entity.setImage(image);
		}
		return entity;
	}

}
