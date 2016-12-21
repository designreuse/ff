package org.ff.rest.settings.resource;

import org.ff.rest.user.resource.UserResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SettingsResource {

	@JsonProperty("user")
	private UserResource user;

}
