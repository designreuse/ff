package org.ff.resource.usergroup;

import java.util.List;

import org.ff.jpa.domain.UserGroup.UserGroupStatus;
import org.ff.resource.AbstractResource;
import org.ff.resource.user.UserResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class UserGroupResource extends AbstractResource {

	public enum UserGroupMetaTag { MATCHING_USERS }

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("status")
	private UserGroupStatus status;

	@JsonProperty("name")
	private String name;

	@JsonProperty("users")
	private List<UserResource> users;

	@JsonProperty("metaTag")
	private UserGroupMetaTag metaTag;

}
