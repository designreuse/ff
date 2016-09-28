package org.ff.resource.algorithmitem;

import java.util.Date;

import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemStatus;
import org.ff.jpa.domain.AlgorithmItem.AlgorithmItemType;
import org.ff.jpa.domain.AlgorithmItem.Operator;
import org.ff.resource.item.ItemResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AlgorithmItemResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("code")
	private String code;

	@JsonProperty("type")
	private AlgorithmItemType type;

	@JsonProperty("status")
	private AlgorithmItemStatus status;

	@JsonProperty("companyItem")
	private ItemResource companyItem;

	@JsonProperty("tenderItem")
	private ItemResource tenderItem;

	@JsonProperty("operator")
	private Operator operator;

	@JsonProperty("conditionalItemCode")
	private String conditionalItemCode;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

}
