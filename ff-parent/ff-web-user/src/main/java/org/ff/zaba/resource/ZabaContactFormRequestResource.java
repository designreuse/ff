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
public class ZabaContactFormRequestResource {

	@JsonProperty("vrsta")
	private String vrsta;

	@JsonProperty("nazivTvrtke")
	private String nazivTvrtke;

	@JsonProperty("oibTvrtke")
	private String oibTvrtke;

	@JsonProperty("nameKontakt")
	private String nameKontakt;

	@JsonProperty("emailKontakt")
	private String emailKontakt;

	@JsonProperty("teleKontakt")
	private String teleKontakt;

	@JsonProperty("office")
	private String office;

	@JsonProperty("poruka")
	private String poruka;

}
