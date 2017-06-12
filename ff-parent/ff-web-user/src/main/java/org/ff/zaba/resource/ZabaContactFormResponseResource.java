package org.ff.zaba.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor @AllArgsConstructor
public class ZabaContactFormResponseResource {

	@JsonProperty("status")
	private Boolean status;

	@JsonProperty("statusGreske")
	private Integer statusGreske;

	@JsonProperty("poruka")
	private String poruka;

	@JsonProperty("obj")
	private String obj;

}
