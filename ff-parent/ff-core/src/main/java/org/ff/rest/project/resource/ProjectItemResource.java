package org.ff.rest.project.resource;

import org.ff.rest.currency.resource.CurrencyResource;
import org.ff.rest.item.resource.ItemResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ProjectItemResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("item")
	private ItemResource item;

	@JsonProperty("value")
	private Object value;

	@JsonProperty("currency")
	private CurrencyResource currency;

}
