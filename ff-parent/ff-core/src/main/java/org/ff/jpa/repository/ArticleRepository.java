package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.Article;
import org.ff.jpa.domain.Article.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

	Page<Article> findAll(Pageable pageable);

	List<Article> findByStatusOrderByLastModifiedDateDesc(ArticleStatus ArticleStatus);

	List<Article> findTop10ByStatusOrderByLastModifiedDateDesc(ArticleStatus ArticleStatus);

	Integer countByStatus(ArticleStatus ArticleStatus);

}
