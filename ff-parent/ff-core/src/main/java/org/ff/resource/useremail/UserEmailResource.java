package org.ff.resource.useremail;

import org.ff.resource.AbstractResource;
import org.ff.resource.email.EmailResource;
import org.ff.resource.tender.TenderResource;
import org.ff.resource.user.UserResource;

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
