package org.ff.rest.useremail.resource;

import org.ff.common.resource.AbstractResource;
import org.ff.rest.email.resource.EmailResource;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.user.resource.UserResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class UserEmailResource extends AbstractResource {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("user")
	private UserResource user;

	@JsonProperty("tender")
	private TenderResource tender;

	@JsonProperty("email")
	private EmailResource email;

}
