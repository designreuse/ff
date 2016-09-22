package org.ff.article.resource;

import java.util.ArrayList;
import java.util.List;

import org.ff.image.resource.ImageResourceAssembler;
import org.ff.jpa.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleResourceAssembler {

	@Autowired
	private ImageResourceAssembler imageResourceAssembler;

	public ArticleResource toResource(Article entity) {
		ArticleResource resource = new ArticleResource();
		resource.setId(entity.getId());
		resource.setName(entity.getName());
		resource.setText(entity.getText());
		if (entity.getImage() != null) {
			resource.setImage(imageResourceAssembler.toResource(entity.getImage()));
		}
		resource.setLastModifiedDate(entity.getLastModifiedDate().toDate());
		return resource;
	}

	public List<ArticleResource> toResources(List<Article> entities) {
		List<ArticleResource> resources = new ArrayList<>();
		for (Article entity : entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

}
