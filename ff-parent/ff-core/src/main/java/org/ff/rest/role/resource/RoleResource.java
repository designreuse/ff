package org.ff.rest.role.resource;

import java.util.List;

import org.ff.common.resource.AbstractResource;
import org.ff.rest.permission.resource.PermissionResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class RoleResource extends AbstractResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("permissions")
	private List<PermissionResource> permissions;

}
