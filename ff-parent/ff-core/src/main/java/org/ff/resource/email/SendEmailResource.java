package org.ff.resource.email;

import java.util.List;

import org.ff.resource.user.UserResource;
import org.ff.resource.usergroup.UserGroupResource;

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
