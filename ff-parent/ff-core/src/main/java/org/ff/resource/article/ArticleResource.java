package org.ff.resource.article;

import java.util.Date;

import org.ff.jpa.domain.Article.ArticleStatus;
import org.ff.resource.image.ImageResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ArticleResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("status")
	private ArticleStatus status;

	@JsonProperty("name")
	private String name;

	@JsonProperty("text")
	private String text;

	@JsonProperty("image")
	private ImageResource image;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
