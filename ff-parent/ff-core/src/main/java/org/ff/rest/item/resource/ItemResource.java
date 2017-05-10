package org.ff.rest.item.resource;

import java.util.Date;
import java.util.List;

import org.ff.jpa.domain.Item.ItemEntityType;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Item.ItemStatus;
import org.ff.jpa.domain.Item.ItemType;
import org.ff.rest.currency.resource.CurrencyResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ItemResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("code")
	private String code;

	@JsonProperty("entityType")
	private ItemEntityType entityType;

	@JsonProperty("status")
	private ItemStatus status;

	@JsonProperty("type")
	private ItemType type;

	@JsonProperty("metaTag")
	private ItemMetaTag metaTag;

	@JsonProperty("summaryItem")
	private Boolean summaryItem;

	@JsonProperty("widgetItem")
	private Boolean widgetItem;

	@JsonProperty("mandatory")
	private Boolean mandatory;

	@JsonProperty("emphasize")
	private Boolean emphasize;

	@JsonProperty("position")
	private Integer position;

	@JsonProperty("text")
	private String text;

	@JsonProperty("help")
	private String help;

	@JsonProperty("options")
	private List<ItemOptionResource> options;

	@JsonProperty("value")
	private Object value;

	@JsonProperty("valueMapped")
	private String valueMapped;

	@JsonProperty("currency")
	private CurrencyResource currency;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
