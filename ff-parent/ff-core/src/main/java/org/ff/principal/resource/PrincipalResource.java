package org.ff.principal.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.ALWAYS)
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class PrincipalResource {

	@JsonProperty("username")
	private String username;

	@JsonProperty("role")
	private String role;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("demoUser")
	private Boolean demoUser;

}
