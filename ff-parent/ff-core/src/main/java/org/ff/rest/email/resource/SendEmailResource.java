package org.ff.rest.email.resource;

import java.util.List;

import org.ff.rest.user.resource.UserResource;
import org.ff.rest.usergroup.resource.UserGroupResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SendEmailResource {

	@JsonProperty("tenderId")
	private Integer tenderId;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("text")
	private String text;

	@JsonProperty("users")
	private List<UserResource> users;

	@JsonProperty("userGroups")
	private List<UserGroupResource> userGroups;

}
