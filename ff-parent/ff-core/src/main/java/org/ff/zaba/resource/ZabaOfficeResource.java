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
public class ZabaOfficeResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("vrsta")
	private String vrsta;

	@JsonProperty("nazivFunkcija")
	private String nazivFunkcija;

	@JsonProperty("adresa")
	private String adresa;

	@JsonProperty("grad")
	private String grad;

	@JsonProperty("postanskiBroj")
	private String postanskiBroj;

	@JsonProperty("idFunkcija")
	private Integer idFunkcija;

}
